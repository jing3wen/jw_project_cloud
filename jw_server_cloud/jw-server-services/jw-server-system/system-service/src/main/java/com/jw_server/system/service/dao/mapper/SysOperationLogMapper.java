package com.jw_server.system.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.system.api.dao.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * Description 系统操作日志表 Mapper 接口
 * Author jingwen
 * Date 2022-09-11 09:32:13
 **/
@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

}
