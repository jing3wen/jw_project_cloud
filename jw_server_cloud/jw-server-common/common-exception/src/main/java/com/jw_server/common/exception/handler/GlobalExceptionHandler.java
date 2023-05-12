package com.jw_server.common.exception.handler;


import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.base.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jingwen
 * @Description 全局异常处理器
 * @DATE 2022/8/19 20:24
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果抛出的的是ServiceException，则调用该方法
     * @param se 业务异常
     * @return Result
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseResult<String> handle(ServiceException se){
        log.info("common-exception拦截到异常: "+se.getMessage());
        return ResponseResult.error(se.getCode(), se.getMessage());
    }

}
