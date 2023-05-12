package com.jw_server.system.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 角色查询表单
 * Author: jingwen
 * DATE: 2022/9/2 17:55
 */
@Data
public class QuerySysUserDTO extends MyPageDTO {

    private String username;

    private String nickname;
}
