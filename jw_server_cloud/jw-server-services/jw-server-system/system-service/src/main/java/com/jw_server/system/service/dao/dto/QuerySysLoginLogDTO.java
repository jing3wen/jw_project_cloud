package com.jw_server.system.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 查询登录日志DTO
 * Author: jingwen
 * DATE: 2022/9/11 12:22
 */
@Data
public class QuerySysLoginLogDTO extends MyPageDTO {

    private String username;

    private String status;
}
