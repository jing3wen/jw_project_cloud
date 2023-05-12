package com.jw_server.system.service.dao.vo;


import com.jw_server.system.service.dao.entity.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: jingwen
 * DATE: 2022/8/30 22:29
 */
@Data
//@ApiModel(description = "返回给前端的菜单树")
public class SysMenuVO extends SysMenu {

    List<SysMenuVO> children;
}
