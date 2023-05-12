package com.jw_server.system.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.service.dao.dto.QuerySysRoleDTO;
import com.jw_server.system.service.dao.entity.SysRole;

import java.util.List;


/**
 * Description 系统角色表 服务类
 * Author jingwen
 * Date 2022-08-31 11:17:21
 **/
public interface ISysRoleService extends IService<SysRole> {

    /**
     * Description:
     * Author: jingwen
     * Date: 2022/9/7 12:40
     **/
    MyPageVO<SysRole> getRolePageList(QuerySysRoleDTO querySysRoleDTO);

    /**
     * Description: 删除角色时，该角色绑定的用户id和菜单id也删除
     * Author: jingwen
     * Date: 2022/8/31 18:36
     **/
    void deleteUserRoleAndRoleMenuWhenRemoveRole(Integer roleId);


    /**
     * Description: 根据用户id查询该用户所有角色id, 角色名
     * Author: jingwen
     * Date: 2022/9/5 22:54
     **/
    List<SysRole> selectAllRoleByUserId(Integer id);

    /**
     * Description: 新增角色时需要验证同名角色和同标识角色
     * Author: jingwen
     * Date: 2022/9/6 19:14
     **/
    void addOrUpdateSysRole(SysRole sysRole);

    /**
     * Description: 内部模块获取用户角色名
     * @author : jingwen
     * Date: 2023/4/24 20:18
     **/
    List<String> getRoleNameListByUserId(Integer userId);
}
