package com.jw_server.common.base.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 返回状态枚举
 * @author : jingwen
 * Date: 2023/4/23 15:36
 **/
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseEnum implements IResponseCode, Serializable {

    SUCCESS(200, "成功"),
    SYSTEM_EXECUTION_ERROR(500, "系统执行出错"),
    USER_UNAUTHORIZED(401, "用户认证失败"),
    USER_ACCESS_FORBIDDEN(403, "权限不足"),
    USERNAME_OR_PASSWORD_ERROR(100, "用户名或密码错误"),
    USER_NOT_EXIST(101, "用户不存在"),
    CLIENT_AUTHENTICATION_FAILED(212, "客户端认证失败"),
    ACCESS_UNAUTHORIZED(213, "未授权"),
    TOKEN_INVALID_OR_EXPIRED(214, "token非法或失效"),
    TOKEN_ACCESS_FORBIDDEN(215, "token禁止访问"),
    FLOW_LIMITING(210, "系统限流"),
    DEGRADATION(220, "系统功能降级"),
    SERVICE_NO_AUTHORITY(221, "服务未授权"),
    ;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private Integer code;

    private String msg;

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }
}
