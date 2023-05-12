package com.jw_server.system.api.client;


import com.jw_server.system.api.dao.entity.SysLoginLog;
import com.jw_server.system.api.dao.entity.SysOperationLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description: 日志
 * @author : jingwen
 * Date: 2023/4/29 17:47
 **/
@FeignClient(name = "system-service", contextId = "SysLogFeignClient")
public interface SysLogFeignClient {

    //内部调用——异步保存操作日志
    @PostMapping("/system/sysOperationLog/inner/asyncSaveOperationLog")
    void asyncSaveOperationLog(@RequestBody SysOperationLog sysOperationLog);

    //内部调用——异步保存登录日志
    @PostMapping("/system/sysLoginLog/inner/asyncSaveLoginLog")
    void asyncSaveLoginLog(@RequestBody SysLoginLog sysLoginLog);
}
