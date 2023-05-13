package com.jw_server.system.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.base.core.context.MySecurityContext;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.mail.utils.EMailUtils;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.common.redis.utils.RedisUtils;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.system.api.dao.entity.SysUser;
import com.jw_server.system.service.core.constants.SysConst;
import com.jw_server.system.service.dao.dto.QuerySysUserDTO;
import com.jw_server.system.service.dao.dto.RegisterUserDTO;
import com.jw_server.system.service.dao.dto.ResetPasswordDTO;
import com.jw_server.system.service.dao.dto.SysForgetPasswordOrUpdateBindDTO;
import com.jw_server.system.service.dao.entity.SysRole;
import com.jw_server.system.service.dao.entity.SysUserRole;
import com.jw_server.system.service.dao.mapper.SysRoleMapper;
import com.jw_server.system.service.dao.mapper.SysUserMapper;
import com.jw_server.system.service.dao.mapper.SysUserRoleMapper;
import com.jw_server.system.service.dao.vo.SysUserVO;
import com.jw_server.system.service.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


/**
 * Description 系统用户表 服务实现类
 * Author jingwen
 * Date 2022-08-29 16:21:58
 **/
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private EMailUtils eMailUtils;

    @Resource
    private Executor completableFutureThreadPool;

    @Resource
    private PasswordEncoder bCryptPasswordEncoder;

    //密码加解密密钥
    @Value("${password.decryptKey}")
    private String decryptKey;


    /**
     * 注册用户——并自动分配角色
     * 要是角色分配失败(原因：用户类型参数错误/角色被停用),
     * 则回滚，注册的用户也会被回滚
     **/
    @Override
    @Transactional
    public void register(RegisterUserDTO registerUserDTO) {
        String username = registerUserDTO.getUsername();
        //对密码进行解密
        String password = getDecryptPassword(registerUserDTO.getPassword());
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            throw new ServiceException(ResponseCode.CODE_400,"用户名或密码为空");
        }
        //检查验证码
        String cacheCode = redisUtils.getCacheObject(SysConst.REGISTER_USER_CODE_CACHE + "_email_" + registerUserDTO.getEmail());
        if(StrUtil.isEmpty(cacheCode) || !registerUserDTO.getCode().equals(cacheCode)){
            throw new ServiceException(ResponseCode.CODE_600, "验证码错误或已过期");
        }
        // 检查是否有同名用户, 只需要随便查找一个属性（主键）即可
        SysUser findSameUserNameUser = getUserByUserName(username);
        //检查同名用户名
        if (ObjectUtil.isNotEmpty(findSameUserNameUser)){
            throw new ServiceException(ResponseCode.CODE_600,"用户名已存在, 请修改用户名");
        }
        //检查邮箱是否已经注册
        SysUser findSameEmailUser = getOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getId)
                .eq(SysUser::getEmail, registerUserDTO.getEmail()));
        if (ObjectUtil.isNotEmpty(findSameEmailUser)){
            throw new ServiceException(ResponseCode.CODE_600,"该邮箱已经注册");
        }
        registerUserDTO.setPassword(password);
        //对存入到数据库密码进行加密
        registerUserDTO.setPassword(bCryptPasswordEncoder.encode(registerUserDTO.getPassword()));
        SysUser addUser  = new SysUser();
        BeanUtil.copyProperties(registerUserDTO, addUser);
        //初始昵称
        addUser.setNickname("初始昵称");
        save(addUser);

        //根据注册用户类型为用户自动分配角色
        SysUser opeUser = getOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getId, SysUser::getUserType)
                .eq(SysUser::getUsername, registerUserDTO.getUsername()));
        setRoleByRegisterUserType(opeUser);

        //清楚缓存
        redisUtils.deleteObject(SysConst.REGISTER_USER_CODE_CACHE + "_email_" + registerUserDTO.getEmail());
    }

    /**
     * 检查是否有同名用户, 只需要随便查找一个属性（主键）即可
     **/
    @Override
    public SysUser getUserByUserName(String username){
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getId)
                .eq(SysUser::getUsername, username);
        SysUser sysUser;
        try {
            sysUser = getOne(queryWrapper);
        }catch (Exception e) {
            // 数据库人为添加了坏数据, 出现多个同名用户
            throw new ServiceException(ResponseCode.CODE_500, "系统错误");
        }
        return sysUser;
    }
//
    @Override
    public MyPageVO<SysUserVO> getUserPageList(QuerySysUserDTO querySysUserDTO) {
        //查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(querySysUserDTO.getUsername())){
            queryWrapper.like(SysUser::getUsername, querySysUserDTO.getUsername());
        }
        if(StrUtil.isNotEmpty(querySysUserDTO.getNickname())){
            queryWrapper.like(SysUser::getNickname, querySysUserDTO.getNickname());
        }
        //用户开启使用假删除
        queryWrapper.eq(SysUser::getIsDeleted, "0");
        IPage<SysUser> userIPage = page(new Page<>(querySysUserDTO.getPageNum(), querySysUserDTO.getPageSize()), queryWrapper);

        //新建userVOList用来存储返回的用户
        List<SysUserVO> userVOList = new ArrayList<>();
        //封装角色信息
        List<SysUser> userList = userIPage.getRecords();
        userList.forEach(user -> {
            SysUserVO userVO = new SysUserVO();
            BeanUtil.copyProperties(user, userVO);
            List<SysRole> roleList = sysRoleMapper.selectAllRoleByUserId(user.getId());
            if (roleList.size() > 0)
                userVO.setRoleList(roleList);
            userVOList.add(userVO);
        });


        return new MyPageVO<>(userIPage.getPages(),
                userIPage.getCurrent(),
                userIPage.getSize(),
                userIPage.getTotal(),
                userVOList);
    }
//

    /**
     * Description: 重置密码
     **/
    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        //传输密码解密
        resetPasswordDTO.setPassword(getDecryptPassword(resetPasswordDTO.getPassword()));
        resetPasswordDTO.setNewPassword(getDecryptPassword(resetPasswordDTO.getNewPassword()));
        String findPassword = sysUserMapper.selectPasswordByUserId(resetPasswordDTO.getUserId());
        if (!bCryptPasswordEncoder.matches(resetPasswordDTO.getPassword(), findPassword)) {
            throw new ServiceException(ResponseCode.CODE_400, "用户原密码输入错误");
        }
        String encodePassword = bCryptPasswordEncoder.encode(resetPasswordDTO.getNewPassword());
        sysUserMapper.updatePasswordByUserId(resetPasswordDTO.getUserId(), encodePassword);
    }


    /**
     * 根据type信息发送验证码: 注册验证码; 找回密码验证码; 绑定邮箱验证码;
     * TODO 建议送到mq里面异步发送
     **/
    @Override
    public void getCodeForType(String email, String phone, String type) {
        int code = new Random().nextInt(900000) + 100000;
        if (StrUtil.isNotEmpty(phone)) {
            log.info(phone + "---" + "手机验证码---" + code);

        } else if (StrUtil.isNotEmpty(email)) {
            log.info(email + "---" + "邮箱验证码---" + code);
            List<String> toUser = new ArrayList<>();
            toUser.add(email);
            if(type.equals(SysConst.REGISTER_USER) ){
                String text = "您的验证码为："+code+", 有效时间为5分钟，请及时注册";
                eMailUtils.sendMailMessage(toUser, "感谢您注册JW网站, 验证码", text);
                redisUtils.setCacheObject(SysConst.REGISTER_USER_CODE_CACHE + "_email_" + email, String.valueOf(code), 5, TimeUnit.MINUTES);
            }else if(type.equals(SysConst.USER_FORGET_PASSWORD)){
                String text = "注意：您正在找回密码，验证码为："+code+", 有效时间为5分钟";
                eMailUtils.sendMailMessage(toUser, "感谢您使用JW网站, 验证码", text);
                redisUtils.setCacheObject(SysConst.USER_FORGET_PASSWORD_CODE_CACHE + "_email_" + email, String.valueOf(code), 5, TimeUnit.MINUTES);
            }else if(type.equals(SysConst.UPDATE_USER_BIND)){
                String text = "注意：您正在绑定该邮箱，验证码为："+code+", 有效时间为5分钟";
                eMailUtils.sendMailMessage(toUser, "感谢您使用JW网站, 验证码", text);
                redisUtils.setCacheObject(SysConst.UPDATE_USER_BIND_CODE_CACHE + "_email_" + email, String.valueOf(code), 5, TimeUnit.MINUTES);
            }else {
                throw new ServiceException(ResponseCode.CODE_400, "参数异常");
            }
        }
    }

    /**
     * 找回密码
     **/
    @Override
    public void updateForgetPassword(SysForgetPasswordOrUpdateBindDTO forgetPasswordDTO) {
        //对密码进行解密
        forgetPasswordDTO.setPassword(getDecryptPassword(forgetPasswordDTO.getPassword()));
        //检查输入数据
        if(StrUtil.isEmpty(forgetPasswordDTO.getPassword())){
            throw new ServiceException(ResponseCode.CODE_600,"输入密码为空");
        }

        if (StrUtil.isNotEmpty(forgetPasswordDTO.getPhone())) {
            log.info(forgetPasswordDTO.getPhone() + "---" + "手机忘记密码---");
        } else if (StrUtil.isNotEmpty(forgetPasswordDTO.getEmail())) {
            log.info(forgetPasswordDTO.getEmail() + "---" + "邮箱忘记密码---");
            //检查验证码
            String cacheCode = redisUtils.getCacheObject(SysConst.USER_FORGET_PASSWORD_CODE_CACHE + "_email_" + forgetPasswordDTO.getEmail());
            if(StrUtil.isEmpty(cacheCode) || !forgetPasswordDTO.getCode().equals(cacheCode)){
                throw new ServiceException(ResponseCode.CODE_600, "验证码错误或已过期");
            }
            //检查待更新密码的用户是否存在/是否被停用
            SysUser findOneByEmail = getOne(new LambdaQueryWrapper<SysUser>()
                    .select(SysUser::getId, SysUser::getStatus)
                    .eq(SysUser::getEmail, forgetPasswordDTO.getEmail()));
            if (ObjectUtil.isEmpty(findOneByEmail)){
                throw new ServiceException(ResponseCode.CODE_600, "该邮箱未注册用户");
            }
            if(findOneByEmail.getStatus().equals(SysConst.USER_FORBID_STATUS)){
                throw new ServiceException(ResponseCode.CODE_403, "该用户被停用");
            }
            //密码加密
            String encodePassword = bCryptPasswordEncoder.encode(forgetPasswordDTO.getPassword());
            update(new LambdaUpdateWrapper<SysUser>()
                    .set(SysUser::getPassword, encodePassword)
                    .eq(SysUser::getId, findOneByEmail.getId()));
            //清除缓存验证码
            redisUtils.deleteObject(SysConst.USER_FORGET_PASSWORD_CODE_CACHE + "_email_" + forgetPasswordDTO.getEmail());
        }
    }

    /**
     * 根据密码 绑定邮箱/手机号 或 更改绑定邮箱/手机号
     **/
    @Override
    public void updateBindByPassword(SysForgetPasswordOrUpdateBindDTO updateBindDTO) {
        LoginUserVO loginUserVO = MySecurityContext.getLoginUserVO();
        String password = getOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getPassword)
                .eq(SysUser::getId, loginUserVO.getId())).getPassword();
        //对密码进行解密
        updateBindDTO.setPassword(getDecryptPassword(updateBindDTO.getPassword()));
        if(StrUtil.isEmpty(updateBindDTO.getPassword()) || !bCryptPasswordEncoder.matches(updateBindDTO.getPassword(), password)){
            throw new ServiceException(ResponseCode.CODE_600, "账户密码错误");
        }
        if (StrUtil.isNotEmpty(updateBindDTO.getPhone())) {
            log.info(loginUserVO.getUsername()+"---更改绑定手机号---"+updateBindDTO.getPhone());
        } else if (StrUtil.isNotEmpty(updateBindDTO.getEmail())) {
            log.info(loginUserVO.getUsername()+"---更改绑定邮箱号---"+updateBindDTO.getEmail());
            //检查验证码
            String cacheCode = redisUtils.getCacheObject(SysConst.UPDATE_USER_BIND_CODE_CACHE + "_email_" + updateBindDTO.getEmail());
            if(StrUtil.isEmpty(cacheCode) || !updateBindDTO.getCode().equals(cacheCode)){
                throw new ServiceException(ResponseCode.CODE_600, "验证码错误或已过期");
            }
            //检查邮箱是否已经被注册
            SysUser findSameEmail = getOne(new LambdaQueryWrapper<SysUser>()
                    .select(SysUser::getId)
                    .eq(SysUser::getEmail, updateBindDTO.getEmail()));
            if (ObjectUtil.isNotEmpty(findSameEmail)){
                throw new ServiceException(ResponseCode.CODE_600, "该邮箱已被注册");
            }
            //更改绑定
            update(new LambdaUpdateWrapper<SysUser>()
                    .set(SysUser::getEmail, updateBindDTO.getEmail())
                    .eq(SysUser::getId, loginUserVO.getId()));
            //清除缓存验证码
            redisUtils.deleteObject(SysConst.UPDATE_USER_BIND_CODE_CACHE + "_email_" + updateBindDTO.getEmail());

        }

    }

    /**
     * 批量删除用户, 用户绑定的属性太多, 此处使用假删除
     **/
    @Override
    @Transactional
    public void deleteUserBatch(List<Integer> ids) {
        //删除用户时，其绑定的角色关系也要删除
        try {
            ids.forEach(userId -> {
                sysUserRoleMapper.deleteUserRoleByUserId(userId);
                //用户绑定的属性太多, 此处使用假删除
                //sysUserService.removeById(userId);
                update(new LambdaUpdateWrapper<SysUser>()
                        .set(SysUser::getIsDeleted, "1")
                        .eq(SysUser::getId, userId));
            });
        }catch (Exception e){
            throw new ServiceException(ResponseCode.CODE_500, "系统异常");
        }

    }

    /**
     * 内部调用——根据用户名查询用户信息
     **/
    @Override
    public SysUser getAuthUserByUsername(String username) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username)
                .or().eq(SysUser::getEmail, username);
        queryWrapper.select(SysUser::getId,
                SysUser::getUsername,
                SysUser::getPassword,
                SysUser::getNickname,
                SysUser::getAvatar,
                SysUser::getEmail,
                SysUser::getPhone,
                SysUser::getSex,
                SysUser::getRemark,
                SysUser::getStatus);
        return sysUserMapper.selectOne(queryWrapper);
    }



    /**
     * Description: 内部调用——异步更新用户最近登录时间
     * Author: jingwen
     * Date: 2022/9/10 18:05
     **/
    public void asyncUpdateUserLastLoginTime(Integer userId){
        CompletableFuture.supplyAsync(()->{
            LambdaUpdateWrapper<SysUser> updateWrapper =new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysUser::getId, userId);
            updateWrapper.set(SysUser::getLastLoginTime, LocalDateTime.now());
            update(updateWrapper);
            return null;
        }, completableFutureThreadPool);
    }

    /**
     * Description: 根据注册用户类型分配初始角色
     * 若userType != null 则匹配注册用户类型
     *
     * Author: jingwen
     * Date: 2023/2/27 0:03
     **/
    public void setRoleByRegisterUserType(SysUser registerUser){
        //根据注册用户类型分配初始角色
        if(StrUtil.isEmpty(registerUser.getUserType())){
            return;
        }
        String roleCode =  registerUser.getUserType();
        if(SysConst.REGISTER_USER_TYPE_LIST.contains(roleCode)){
            log.info("当前注册用户类型: "+roleCode+" , 开始为注册用户自动分配角色");
            SysRole findRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                    .select(SysRole::getId)
                    .eq(SysRole::getStatus, "1")
                    .eq(SysRole::getRoleCode, roleCode));
            //检查该角色是否被误删除
            if(ObjectUtil.isEmpty(findRole)){
                throw new ServiceException(ResponseCode.CODE_600, "初始注册角色可能被删除或停用, 请联系管理员");
            }
            if(ObjectUtil.isNotEmpty(registerUser.getId())){
                SysUserRole addUserRole = new SysUserRole(registerUser.getId(), findRole.getId());
                sysUserRoleMapper.insert(addUserRole);
                log.info("已经成功分配角色——"+roleCode);
            }
        }else {
            throw new ServiceException(ResponseCode.CODE_400, "注册用户类型参数错误");
        }
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


}
