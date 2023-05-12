package com.jw_server.auth.service.impl;

import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.security.dao.UserDetailsVO;
import com.jw_server.system.api.client.SysUserFeignClient;
import com.jw_server.system.api.dao.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/29 20:44
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserFeignClient sysUserFeignClient;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 远程查询登录用户信息
        SysUser queryUser = sysUserFeignClient.getAuthUserByUsername(username).getData();

        /*
         * Description:
         * 丢出用户名不存在的异常，供userLogin方法捕获
         */
        if (Objects.isNull(queryUser)){
            throw new ServiceException(ResponseCode.CODE_403,"用户名不存在");
        }
        if (queryUser.getStatus().equals("0")){
            throw new ServiceException(ResponseCode.CODE_403,"用户已被停用");
        }
        return createLoginSysUserVO(queryUser);
    }


    /**
     * 封装登陆返回类
     * param queryUser
     * return
     */
    public UserDetailsVO createLoginSysUserVO(SysUser queryUser){

        //角色名称和权限按钮  在LoginServiceImpl中封装
        // 把数据封装成UserDetails返回
        return UserDetailsVO.builder()
                .id(queryUser.getId())
                .username(queryUser.getUsername())
                .password(queryUser.getPassword())
                .nickname(queryUser.getNickname())
                .avatar(queryUser.getAvatar())
                .permissionList(new ArrayList<>()) //此处为需要初始化new ArrayList<>(); 因为authorities为null会抛出异常
                .email(queryUser.getEmail())
                .phone(queryUser.getPhone())
                .sex(queryUser.getSex())
                .remark(queryUser.getRemark())
                .build();
    }



}
