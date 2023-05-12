package com.jw_server.auth.service;

import com.jw_server.auth.dao.LoginSysUserDTO;
import com.jw_server.common.base.dao.LoginUserVO;

public interface LoginService {

    /**
     * Description: 用户登录方法，校验验证码功能在LoginController中完成，所以该方法只是认证username和password
     * Author: jingwen
     * Date: 2022/8/30 9:39
     **/
    LoginUserVO userLogin(LoginSysUserDTO loginSysUserDTO);

    /**
     * Description: 登出
     * Author: jingwen
     * Date: 2022/8/30 16:29
     **/
    void userLogout(String token);
}
