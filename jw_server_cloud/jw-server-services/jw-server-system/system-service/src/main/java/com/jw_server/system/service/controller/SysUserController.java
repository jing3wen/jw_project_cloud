package com.jw_server.system.service.controller;

import com.jw_server.common.base.core.context.MySecurityContext;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.api.dao.entity.SysUser;
import com.jw_server.system.service.dao.dto.*;
import com.jw_server.system.service.dao.vo.SysUserVO;
import com.jw_server.system.service.service.ISysRoleService;
import com.jw_server.system.service.service.ISysUserRoleService;
import com.jw_server.system.service.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;




/**
 * author jingwen
 * Description 系统用户表 前端控制器
 * Date 2022-08-29 16:21:58
 */
@Slf4j
@RestController
@RequestMapping("/system/sysUser")
public class SysUserController {

    // 初始化密码
    @Value("${password.initPassword}")
    private String initPassword;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysUserRoleService sysUserRoleService;


    /**
     * Description 新增
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @SysLog(logModule= LogModuleConst.SysUserModule, logType = LogTypeConst.ADD, logDesc = "新增用户")
    @PreAuthorize("hasAuthority('system:sysUser:add')")
    @PostMapping("/add")
    public ResponseResult<Object> add(@RequestBody RegisterUserDTO registerUserDTO) {
        //添加用户时，对密码进行加密
        if (registerUserDTO.getPassword()==null || registerUserDTO.getPassword().equals("")){
            registerUserDTO.setPassword(initPassword);
        }
        sysUserService.register(registerUserDTO);
        return ResponseResult.success();
    }


    /**
     * Description 更新
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @SysLog(logModule=LogModuleConst.SysUserModule, logType = LogTypeConst.UPDATE, logDesc = "编辑用户")
    @PreAuthorize("hasAuthority('system:sysUser:update')")  //防止注册新用户不能更新个人信息，或者改为为注册用户默认分配一个带权限的角色
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @SysLog(logModule=LogModuleConst.SysUserModule, logType = LogTypeConst.DELETE, logDesc = "删除用户")
    @PreAuthorize("hasAuthority('system:sysUser:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        sysUserService.deleteUserBatch(ids);
        return ResponseResult.success();
    }

    /**
     * Description 根据id查询数据
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @GetMapping("/findOne")
    public ResponseResult<SysUser> findOne(@RequestParam Integer id) {
        return ResponseResult.success(sysUserService.getById(id));
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-08-29 16:21:58
     **/
    @PreAuthorize("hasAuthority('system:sysUser:query')")
    @PostMapping("/getPageList")
    public ResponseResult<MyPageVO<SysUserVO>> getPageList(@RequestBody QuerySysUserDTO querySysUserDTO) {
        return ResponseResult.success(sysUserService.getUserPageList(querySysUserDTO));
    }

    /**
     * Description 注册
     * Author jingwen
     * Date 2022/8/20 10:41
     **/
    @SysLog(logModule=LogModuleConst.SysUserModule, logType = LogTypeConst.ADD, logDesc = "用户注册")
    @PostMapping("/register")
    public ResponseResult<Object> register(@RequestBody RegisterUserDTO sysUser){
        sysUserService.register(sysUser);
        return ResponseResult.success();
    }


    /**
     * Description: 根据用户id查询该用户所有角色
     * Author: jingwen
     * Date: 2022/9/5 22:54
     **/
    @GetMapping("/findRoleByUserId")
    public ResponseResult<List> findRoleByUserId(@RequestBody SysUser sysUser){

        return ResponseResult.success(sysRoleService.selectAllRoleByUserId(sysUser.getId()));
    }


    /**
     * Description: 编辑用户角色信息
     * Author: jingwen
     * Date: 2022/9/6 8:50
     **/
    @SysLog(logModule= LogModuleConst.SysUserModule, logType = LogTypeConst.UPDATE, logDesc = "更新用户角色信息")
    @PreAuthorize("hasAuthority('system:sysUser:editRole')")
    @PostMapping("/updateUserRole")
    public ResponseResult<Object> updateUserRole(@RequestBody UserRoleDTO userRoleDTO){

        sysUserRoleService.updateUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleIds());
        return ResponseResult.success();
    }

    /**
     * Description: 重置用户密码
     * Author: jingwen
     * Date: 2022/9/10 20:17
     **/
    @SysLog(logModule=LogModuleConst.SysUserModule, logType = LogTypeConst.UPDATE, logDesc = "重置用户密码")
    @PreAuthorize("hasAuthority('system:sysUser:resetPassword')")
    @PostMapping("/resetPassword")
    public ResponseResult<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        sysUserService.resetPassword(resetPasswordDTO);
        return ResponseResult.success();
    }


    /**
     * Description: 获取验证码
     *
     * type表示获取验证码的作用：注册用户/忘记密码/更改绑定绑定信息
     *
     * Author: jingwen
     * Date: 2023/2/25 19:42
     **/
    @GetMapping("/getCodeForType")
    public ResponseResult<Object> getCodeForType(@RequestParam("email") String email,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("type") String type){
        sysUserService.getCodeForType(email, phone, type);
        return ResponseResult.success();
    }

    /**
     * Description: 找回密码
     * Author: jingwen
     * Date: 2023/2/25 23:26
     **/
    @PostMapping("/updateForgetPassword")
    public ResponseResult<Object> updateForgetPassword(@RequestBody SysForgetPasswordOrUpdateBindDTO forgetPasswordDTO){

        sysUserService.updateForgetPassword(forgetPasswordDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 根据密码 绑定邮箱/手机号 或 更改绑定邮箱/手机号
     * Author: jingwen
     * Date: 2023/2/26 13:46
     **/
    @PostMapping("/updateBindByPassword")
    public ResponseResult<Object> updateBindByPassword(@RequestBody SysForgetPasswordOrUpdateBindDTO updateBindDTO){

        sysUserService.updateBindByPassword(updateBindDTO);
        return ResponseResult.success();
    }

    /**
     * Description: 内部调用——根据用户名查询用户信息
     * @author : jingwen
     * Date: 2023/4/24 20:55
     **/
    @GetMapping("/inner/getAuthUserByUsername")
    public ResponseResult<SysUser> getAuthUserByUsername(String username){
        //log.info("内部调用"+ MySecurityContext.getLoginUserVO());
        return ResponseResult.success(sysUserService.getAuthUserByUsername(username));
    }

    /**
     * Description: 内部调用——异步更新用户最近登录时间
     * @author : jingwen
     * Date: 2023/4/24 20:55
     **/
    @PutMapping("/inner/asyncUpdateUserLastLoginTime")
    void asyncUpdateUserLastLoginTime(Integer userId){
        sysUserService.asyncUpdateUserLastLoginTime(userId);
    }


    @GetMapping("/testAuth")
    public ResponseResult<LoginUserVO> testAuth(){
        log.info("内部调用"+ MySecurityContext.getLoginUserVO());
        return ResponseResult.success(MySecurityContext.getLoginUserVO());
    }

}

