package com.jw_server.system.service.controller;

import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.system.service.dao.dto.QuerySysMenuDTO;
import com.jw_server.system.service.dao.entity.SysMenu;
import com.jw_server.system.service.dao.vo.SysMenuVO;
import com.jw_server.system.service.service.ISysMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * author jingwen
 * Description 菜单表 前端控制器
 * Date 2022-08-30 18:58:10
 */
@RestController
@RequestMapping("/system/sysMenu")
public class SysMenuController {

    @Resource
    private ISysMenuService sysMenuService;

    /**
     * Description 新增
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @SysLog(logModule= LogModuleConst.SysMenuModule, logType = LogTypeConst.ADD, logDesc = "新增菜单")
    @PreAuthorize("hasAuthority('system:sysMenu:add')")
    @PostMapping("/add")
    public ResponseResult<Object> add(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return ResponseResult.success();
    }
    /**
     * Description 更新
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @SysLog(logModule=LogModuleConst.SysMenuModule, logType = LogTypeConst.UPDATE, logDesc = "更新菜单")
    @PreAuthorize("hasAuthority('system:sysMenu:update')")
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @SysLog(logModule=LogModuleConst.SysMenuModule, logType = LogTypeConst.DELETE, logDesc = "删除菜单")
    @PreAuthorize("hasAuthority('system:sysMenu:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {

        sysMenuService.deleteMenu(ids);
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @GetMapping("/findAll")
    public ResponseResult<List<SysMenuVO>> findAll() {
        return ResponseResult.success(sysMenuService.sysMenuList());
    }


    /**
     * Description 分页查询
     * Author jingwen
     * Date 2022-08-30 18:58:10
     **/
    @PreAuthorize("hasAuthority('system:sysMenu:query')")
    @PostMapping("/getPageList")
    public ResponseResult<List<SysMenuVO>> getPageList(@RequestBody QuerySysMenuDTO querySysMenuDTO) {
        return ResponseResult.success(sysMenuService.getMenuPageList(querySysMenuDTO));
    }


    /**
     * Description: 根据用户id查询该用户的路由信息(目录和子菜单)
     * Author: jingwen
     * Date: 2022/11/3 20:17
     **/
    @GetMapping("/getMenusAndDirectoryByUserId")
    public ResponseResult<List<SysMenuVO>> getMenusAndDirectoryByUserId(Integer userId){
        return ResponseResult.success(sysMenuService.selectMenusAndDirectoryByUserId(userId,true));
    }

    /**
     * Description: 内部调用——获取权限信息
     * @author : jingwen
     * Date: 2023/4/24 20:21
     **/
    @GetMapping("/inner/getPermissionsByUserId")
    public ResponseResult<List<String>> getPermissionsByUserId(@RequestParam("userId") Integer userId) {
        return ResponseResult.success(sysMenuService.selectPermissionsByUserId(userId));
    }

}

