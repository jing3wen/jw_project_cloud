package com.jw_server.system.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.service.dao.dto.QuerySysOperationLogDTO;
import com.jw_server.system.api.dao.entity.SysOperationLog;
import com.jw_server.system.service.dao.mapper.SysOperationLogMapper;
import com.jw_server.system.service.service.ISysOperationLogService;
import org.springframework.stereotype.Service;

/**
 * Description 系统操作日志表 服务实现类
 * Author jingwen
 * Date 2022-09-11 09:32:13
 **/
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements ISysOperationLogService {

    /**
     * Description: 查询操作日志分页
     **/
    @Override
    public MyPageVO<SysOperationLog> getOperationLogPageList(QuerySysOperationLogDTO querySysOperationLogDTO) {

        LambdaQueryWrapper<SysOperationLog> queryWrapper = new LambdaQueryWrapper<>();
        //操作模块
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getOptModule())){
            queryWrapper.like(SysOperationLog::getOptModule, querySysOperationLogDTO.getOptModule());
        }
        //操作类型
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getOptType())){
            queryWrapper.like(SysOperationLog::getOptType, querySysOperationLogDTO.getOptType());
        }
        //操作人
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getOptUser())){
            queryWrapper.like(SysOperationLog::getOptUser, querySysOperationLogDTO.getOptUser());
        }
        //操作状态
        if(StrUtil.isNotEmpty(querySysOperationLogDTO.getStatus())){
            queryWrapper.like(SysOperationLog::getStatus, querySysOperationLogDTO.getStatus());
        }
        queryWrapper.orderByDesc(SysOperationLog::getCreateTime);
        return new MyPageVO<>(
                page(new Page<>(querySysOperationLogDTO.getPageNum(),
                                querySysOperationLogDTO.getPageSize()),
                        queryWrapper)
        );
    }
}
