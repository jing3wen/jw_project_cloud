package com.jw_server.system.service.dao.vo;


import com.jw_server.system.api.dao.entity.SysUser;
import com.jw_server.system.service.dao.entity.SysRole;
import lombok.Data;

import java.util.List;

/**
 * Description: 后端用户列表类
 * Author: jingwen
 * DATE: 2022/8/31 18:55
 */
@Data
public class SysUserVO extends SysUser {

    List<SysRole> roleList;
}
