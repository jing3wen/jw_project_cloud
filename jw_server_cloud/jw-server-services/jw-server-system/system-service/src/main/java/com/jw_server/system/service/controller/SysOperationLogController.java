package com.jw_server.system.service.controller;

import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.api.dao.entity.SysOperationLog;
import com.jw_server.system.service.dao.dto.QuerySysOperationLogDTO;
import com.jw_server.system.service.service.ISysOperationLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * author jingwen
 * Description 系统操作日志表 前端控制器
 * Date 2022-09-11 09:32:13
 */
@RestController
@RequestMapping("/system/sysOperationLog")
public class SysOperationLogController {

    @Resource
    private ISysOperationLogService sysOperationLogService;

    @Resource
    private Executor completableFutureThreadPool;


    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-11 09:32:13
     **/
    @PreAuthorize("hasAuthority('system:sysOperationLog:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        sysOperationLogService.removeByIds(ids);
        return ResponseResult.success();
    }

    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-11 09:32:13
     **/
    @PreAuthorize("hasAuthority('system:sysOperationLog:query')")
    @PostMapping("/getPageList")
    public ResponseResult<MyPageVO<SysOperationLog>> getPageList(@RequestBody QuerySysOperationLogDTO querySysOperationLogDTO) {

        return ResponseResult.success(sysOperationLogService.getOperationLogPageList(querySysOperationLogDTO));
    }

    /**
     * Description: 内部调用——保存操作日志
     * Author: jingwen
     * Date: 2023/4/30 12:22
     **/
    @PostMapping("/inner/asyncSaveOperationLog")
    public void asyncSaveOperationLog(@RequestBody SysOperationLog sysOperationLog){
        CompletableFuture.supplyAsync(()->{
            sysOperationLogService.save(sysOperationLog);
            return null;
        }, completableFutureThreadPool);
    }

}

