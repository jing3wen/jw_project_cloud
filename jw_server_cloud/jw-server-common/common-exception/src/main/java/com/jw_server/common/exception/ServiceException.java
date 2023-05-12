package com.jw_server.common.exception;

import com.jw_server.common.base.result.ResponseEnum;
import lombok.Getter;

/**
 * @author jingwen
 * @Description 自定义异常服务
 * @DATE 2022/8/19 20:25
 */
@Getter
public class ServiceException extends RuntimeException{

    private final Integer code;

    public ServiceException(ResponseEnum responseEnum, String msg) {
        super(msg);
        this.code = responseEnum.getCode();
    }

    public ServiceException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.getCode();
    }

    public ServiceException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
