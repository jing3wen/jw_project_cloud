package com.jw_server.system.service.dao.dto;

import com.jw_server.common.mybatis.page.MyPageDTO;
import lombok.Data;

/**
 * Description: 菜单查询表单
 * Author: jingwen
 * DATE: 2022/9/7 12:51
 */
@Data
public class QuerySysMenuDTO extends MyPageDTO {

    private String menuName;

}
