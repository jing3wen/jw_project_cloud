package com.jw_server.system.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 查询操作日志表单
 * Author: jingwen
 * DATE: 2022/9/11 12:22
 */
@Data
public class QuerySysOperationLogDTO extends MyPageDTO {

    private String optModule;

    private String optType;

    private String optUser;

    private String status;
}
