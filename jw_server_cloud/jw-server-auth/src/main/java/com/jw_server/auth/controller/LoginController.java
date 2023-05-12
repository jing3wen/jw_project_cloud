package com.jw_server.auth.controller;

import com.jw_server.common.base.core.constants.BaseConfigConst;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.auth.dao.LoginSysUserDTO;
import com.jw_server.common.base.dao.LoginUserVO;
import com.jw_server.auth.service.LoginService;
import com.jw_server.common.base.result.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



/**
 * Description: 登录controller类
 * Author: jingwen
 * DATE: 2022/8/30 9:04
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Resource
    private LoginService loginService;


    /**
     * Description: 用户登录
     * Author: jingwen
     * Date: 2022/8/30 9:29
     **/
    @SysLog(logModule = LogModuleConst.UserLoginModule, logType = LogTypeConst.USER_LOGIN)
    @PostMapping("/login/userLogin")
    public ResponseResult<LoginUserVO> userLogin(@RequestBody LoginSysUserDTO loginSysUserDTO){
        //校验验证码

        return ResponseResult.success(loginService.userLogin(loginSysUserDTO));
    }

    /**
     * Description: 登出
     * Author: jingwen
     * Date: 2022/8/30 16:23
     **/
    @PostMapping("/login/userLogout")
    public ResponseResult<Object> userLogout(HttpServletRequest request){
        String token = request.getHeader(BaseConfigConst.TOKEN_HEADER);
        loginService.userLogout(token);
        return ResponseResult.success();
    }
}
