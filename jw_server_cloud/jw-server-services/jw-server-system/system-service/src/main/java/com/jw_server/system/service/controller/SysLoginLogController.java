package com.jw_server.system.service.controller;


import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.api.dao.entity.SysLoginLog;
import com.jw_server.system.service.dao.dto.QuerySysLoginLogDTO;
import com.jw_server.system.service.service.ISysLoginLogService;
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
 * Description 用户登录日志 前端控制器
 * Date 2022-09-11 17:06:04
 */
@RestController
@RequestMapping("/system/sysLoginLog")
public class SysLoginLogController {

    @Resource
    private ISysLoginLogService sysLoginLogService;

    @Resource
    private Executor completableFutureThreadPool;

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-09-11 17:06:04
     **/
    @PreAuthorize("hasAuthority('system:sysLoginLog:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        sysLoginLogService.removeByIds(ids);
        return ResponseResult.success();
    }


    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-09-11 17:06:04
     **/
    @PreAuthorize("hasAuthority('system:sysLoginLog:query')")
    @PostMapping("/getPageList")
    public ResponseResult<MyPageVO<SysLoginLog>> getPageList(@RequestBody QuerySysLoginLogDTO querySysLoginLogDTO) {

        return ResponseResult.success(sysLoginLogService.getSysLoginPageList(querySysLoginLogDTO));
    }

    /**
     * Description: 内部调用——保存登录日志
     * @author : jingwen
     * Date: 2023/4/30 12:22
     **/
    @PostMapping("/inner/asyncSaveLoginLog")
    public void asyncSaveLoginLog(@RequestBody SysLoginLog sysLoginLog){
        CompletableFuture.supplyAsync(()->{
            sysLoginLogService.save(sysLoginLog);
            return null;
        }, completableFutureThreadPool);
    }
}

