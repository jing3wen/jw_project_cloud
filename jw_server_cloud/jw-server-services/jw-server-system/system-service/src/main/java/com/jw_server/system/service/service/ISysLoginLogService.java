package com.jw_server.system.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.service.dao.dto.QuerySysLoginLogDTO;
import com.jw_server.system.api.dao.entity.SysLoginLog;


/**
 * Description 用户登录日志 服务类
 * Author jingwen
 * Date 2022-09-11 17:06:04
 **/
public interface ISysLoginLogService extends IService<SysLoginLog> {

    /**
     * Description: 查询登录日志分页
     * @author : jingwen
     * Date: 2023/4/30 21:06
     **/
    MyPageVO<SysLoginLog> getSysLoginPageList(QuerySysLoginLogDTO querySysLoginLogDTO);
}
