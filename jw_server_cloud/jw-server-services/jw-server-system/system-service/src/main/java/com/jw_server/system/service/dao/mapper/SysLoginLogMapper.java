package com.jw_server.system.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.system.api.dao.entity.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * Description 用户登录日志 Mapper 接口
 * Author jingwen
 * Date 2022-09-11 17:06:04
 **/
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

}
