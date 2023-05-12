package com.jw_server.system.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.system.service.dao.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Description 用户角色关联表 Mapper 接口
 * Author jingwen
 * Date 2022-08-31 11:18:06
 **/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * Description: 根据用户id删除所有绑定的角色
     * Author: jingwen
     * Date: 2022/9/6 9:04
     **/
    int deleteUserRoleByUserId(@Param("userId") Integer userId);

    /**
     * Description: 根据角色id删除所有绑定的用户
     * Author: jingwen
     * Date: 2022/9/6 9:04
     **/
    int deleteUserRoleByRoleId(@Param("roleId") Integer roleId);

    /**
     * Description: 批量新增用户角色关系
     * Author: jingwen
     * Date: 2022/9/11 15:26
     **/
    int insertBatchUserRole(List<SysUserRole> userRoleList);
}
