package com.jw_server.system.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.system.service.dao.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Description 菜单表 Mapper 接口
 * Author jingwen
 * Date 2022-08-30 18:58:10
 **/
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    /**
     * Description: 根据用户id查询权限按钮，多表查询sys_user_role, sys_role, sys_role_menu, sys_menu
     * Author: jingwen
     * Date: 2022/8/30 20:36
     **/
    List<String> selectPermissionsByUserId(Integer userId);


    /**
     * Description: 根据用户id查询目录和子菜单，多表查询 sys_user_role, sys_role, sys_role_menu, sys_menu
     * Author: jingwen
     * Date: 2022/11/3 19:46
     **/
    List<SysMenu> selectMenusAndDirectoryByUserId(Integer userId);



}
