package com.jw_server.system.api.client;


import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.system.api.dao.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "system-service", contextId = "SysUserFeignClient")
public interface SysUserFeignClient {

    // 远程查询登录用户信息
    @GetMapping("/system/sysUser/inner/getAuthUserByUsername")
    ResponseResult<SysUser> getAuthUserByUsername(@RequestParam("username") String username);

    // 远程更新用户最后登录时间——异步
    @PutMapping("/system/sysUser/inner/asyncUpdateUserLastLoginTime")
    void asyncUpdateUserLastLoginTime(Integer userId);

//    @PostMapping("/getCurrentLoginUser")
//    LoginUserVO getCurrentLoginUser();
    // 远程获取用户角色名
    @GetMapping("/system/sysRole/inner/getRoleNameListByUserId")
    ResponseResult<List<String>> getRoleNameListByUserId(@RequestParam("userId") Integer userId);

    // 远程获取用户权限按钮
    @GetMapping("/system/sysMenu/inner/getPermissionsByUserId")
    ResponseResult<List<String>> getPermissionsByUserId(@RequestParam("userId") Integer userId);


}
