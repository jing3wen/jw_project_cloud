package com.jw_server.auth.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.jw_server.auth.dao.LoginSysUserDTO;
import com.jw_server.common.base.core.constants.BaseConfigConst;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.auth.service.LoginService;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.redis.utils.RedisUtils;
import com.jw_server.common.security.core.utils.JwtUtils;
import com.jw_server.common.security.dao.UserDetailsVO;
import com.jw_server.system.api.client.SysUserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysUserFeignClient sysUserFeignClient;

    //密码加解密密钥
    @Value("${password.decryptKey}")
    private String decryptKey;


    /**
     * Description: 用户登录方法，校验验证码功能在LoginController中完成，所以该方法只是认证username和password
     * Author: jingwen
     * Date: 2022/8/30 9:39
     **/
    @Override
    public LoginUserVO userLogin(LoginSysUserDTO loginSysUserDTO) {
        //对密码进行解密
        loginSysUserDTO.setPassword(getDecryptPassword(loginSysUserDTO.getPassword()));
        //AuthenticationManager的authenticate方法来进行用户认证, authenticate方法会调用UserDetailsService的loadUserByUsername方法来认证
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginSysUserDTO.getUsername(), loginSysUserDTO.getPassword());
        Authentication authenticateResult;
        try {
            authenticateResult = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            //log.error(e.getMessage());
            if(e.getMessage().equals("用户名不存在"))
                throw new ServiceException(ResponseCode.CODE_403,"用户名不存在");
            else if(e.getMessage().equals("用户已被停用"))
                throw new ServiceException(ResponseCode.CODE_403,"用户已被停用");
            throw new ServiceException(ResponseCode.CODE_403,"密码错误");
        }
        if (Objects.isNull(authenticateResult)){
            throw new ServiceException(ResponseCode.CODE_500,"登录失败");
        }
        //如果认证通过，使用userId生成一个jwt
        UserDetailsVO userDetailsVO = (UserDetailsVO) authenticateResult.getPrincipal();

        String userId = userDetailsVO.getId().toString();
        String token = JwtUtils.createJWT(userId);
        userDetailsVO.setToken(token);

        //String subToken = StringUtils.substringAfterLast(token, ".");
        String subToken = token.substring(token.lastIndexOf(".")+1);

        /*
        * 不建议直接返回userDetailsVO，因为里面有一些属性前端不需要
         */
        //TODO 此处两个调用服务可以异步，然后阻塞等待结果汇总
        //查询用户所有角色信息
        userDetailsVO.setRoleList(sysUserFeignClient.getRoleNameListByUserId(userDetailsVO.getId()).getData());
        //存储所有权限按钮
        userDetailsVO.setPermissionList(sysUserFeignClient.getPermissionsByUserId(userDetailsVO.getId()).getData());

        LoginUserVO loginUserVO = copyBeanFromUserDetailsVO(userDetailsVO);

        //异步——更新登录时间, 且不需要阻塞等待结果
        sysUserFeignClient.asyncUpdateUserLastLoginTime(loginUserVO.getId());
        //把完整的用户信息存入redis, token的第三段字符串为key
        //登录用户redis中的过期时间和jwt的过期时间一样
        redisUtils.setCacheObject("login-tokens:"+subToken, userDetailsVO, BaseConfigConst.TOKEN_TTL, TimeUnit.MILLISECONDS);
        return loginUserVO;
    }


    @Override
    public void userLogout(String token) {
        //删除redis中的缓存信息
        String subToken = token.substring(token.lastIndexOf(".")+1);
        redisUtils.deleteObject("login-tokens:"+subToken);
    }

    /**
     * 对密码进行解密
     **/
    public String getDecryptPassword(String password){

        log.info("开始对密码进行解密——"+password);
        String decryptPassword = "";
        try {
            decryptPassword = new String(SecureUtil.aes(decryptKey.getBytes(StandardCharsets.UTF_8)).decrypt(password));
        }catch (Exception e){
            throw new ServiceException(ResponseCode.CODE_403, "密码错误");
        }
        log.info("解密成功");
        return decryptPassword;
    }

    public LoginUserVO copyBeanFromUserDetailsVO(UserDetailsVO userDetailsVO) {
        return LoginUserVO.builder()
                .id(userDetailsVO.getId())
                .username(userDetailsVO.getUsername())
                .nickname(userDetailsVO.getNickname())
                .avatar(userDetailsVO.getAvatar())
                .roleList(userDetailsVO.getRoleList())
                .token(userDetailsVO.getToken())
                .permissionList(userDetailsVO.getPermissionList())
                .email(userDetailsVO.getEmail())
                .phone(userDetailsVO.getPhone())
                .sex(userDetailsVO.getSex())
                .remark(userDetailsVO.getRemark())
                .build();
    }




}
