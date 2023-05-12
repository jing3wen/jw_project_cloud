package com.jw_server.auth.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 用户登录类
 * @author : jingwen
 * DATE: 2022/8/30 9:35
 */
@Data
@ApiModel(value = "LoginSysUser对象", description = "用户登录类")
public class LoginSysUserDTO {

    //注意, 此处的"username"不要修改, 因为在日志aop上需要获取这个属性
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;
}
