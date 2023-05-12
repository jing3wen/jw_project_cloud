package com.jw_server.system.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 用户查询表单
 * Author: jingwen
 * DATE: 2022/9/7 12:37
 */
@Data
public class QuerySysRoleDTO extends MyPageDTO {

    private String roleCode;

    private String roleName;
}
