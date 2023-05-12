package com.jw_server.system.service.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.service.dao.dto.QuerySysOperationLogDTO;
import com.jw_server.system.api.dao.entity.SysOperationLog;

/**
 * Description 系统操作日志表 服务类
 * Author jingwen
 * Date 2022-09-11 09:32:13
 **/
public interface ISysOperationLogService extends IService<SysOperationLog> {

    /**
     * Description: 查询操作日志分页
     * @author : jingwen
     * Date: 2022/9/11 12:28
     **/
    MyPageVO<SysOperationLog> getOperationLogPageList(QuerySysOperationLogDTO querySysOperationLogDTO);
}
