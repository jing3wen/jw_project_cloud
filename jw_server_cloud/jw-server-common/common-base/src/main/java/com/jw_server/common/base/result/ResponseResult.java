package com.jw_server.common.base.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author jingwen
 * @Description
 * @DATE 2022/8/17 10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    public ResponseResult(ResponseEnum responseEnum){
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
        this.data=null;
    }
    public static <T> ResponseResult<T> success() {
        return success(ResponseEnum.SUCCESS);
    }

    public static <T> ResponseResult<T> success(ResponseEnum responseEnum){
        return success(responseEnum, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return success(ResponseEnum.SUCCESS, data);
    }

    public static <T> ResponseResult<T> success(ResponseEnum responseEnum, T data){
        return success(responseEnum.getCode(), responseEnum.getMsg(), data);
    }

    private static <T> ResponseResult<T> success(Integer code, String msg, T data){
        return new ResponseResult<>(code, msg, data);
    }



    public static <T> ResponseResult<T> error() {
        return error(ResponseEnum.SYSTEM_EXECUTION_ERROR);
    }

    public static <T> ResponseResult<T> error(ResponseEnum responseEnum) {
        return error(responseEnum.getCode(), responseEnum.getMsg());
    }

    public static <T> ResponseResult<T> error(String msg) {
        return error(ResponseEnum.SYSTEM_EXECUTION_ERROR.getCode(), msg);
    }

    public static <T> ResponseResult<T> error(Integer code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }



}
