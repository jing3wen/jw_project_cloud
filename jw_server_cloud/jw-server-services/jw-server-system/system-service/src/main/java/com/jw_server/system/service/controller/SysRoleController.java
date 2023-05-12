package com.jw_server.system.service.controller;

import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.log.annotation.SysLog;
import com.jw_server.common.log.constants.LogModuleConst;
import com.jw_server.common.log.constants.LogTypeConst;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.service.dao.dto.QuerySysRoleDTO;
import com.jw_server.system.service.dao.dto.SysRoleMenuDTO;
import com.jw_server.system.service.dao.entity.SysRole;
import com.jw_server.system.service.service.ISysRoleMenuService;
import com.jw_server.system.service.service.ISysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * Description 系统角色表 前端控制器
 * @author : jingwen
 * Date 2022-08-31 11:17:21
 */
@RestController
@RequestMapping("/system/sysRole")
public class SysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * Description 新增
     * @author : jingwen
     * Date 2022-08-31 11:17:21
     **/
    @SysLog(logModule= LogModuleConst.SysRoleModule, logType = LogTypeConst.ADD, logDesc = "新增角色")
    @PreAuthorize("hasAuthority('system:sysRole:add')")
    @PostMapping("/add")
    public ResponseResult<Object> add(@RequestBody SysRole sysRole) {
        sysRoleService.addOrUpdateSysRole(sysRole);
        return ResponseResult.success();
    }

    /**
     * Description 更新
     * @author : jingwen
     * Date 2022-08-31 11:17:21
     **/
    @SysLog(logModule= LogModuleConst.SysRoleModule, logType = LogTypeConst.UPDATE, logDesc = "更新角色")
    @PreAuthorize("hasAuthority('system:sysRole:update')")
    @PostMapping("/update")
    public ResponseResult<Object> update(@RequestBody SysRole sysRole) {
        sysRoleService.addOrUpdateSysRole(sysRole);
        return ResponseResult.success();
    }

    /**
     * Description 批量删除
     * @author : jingwen
     * Date 2022-08-31 11:17:21
     **/
    @SysLog(logModule=LogModuleConst.SysRoleModule, logType = LogTypeConst.DELETE, logDesc = "删除角色")
    @PreAuthorize("hasAuthority('system:sysRole:delete')")
    @PostMapping("/deleteBatch")
    public ResponseResult<Object> deleteBatch(@RequestBody List<Integer> ids) {
        for(Integer roleId:ids){
            sysRoleService.deleteUserRoleAndRoleMenuWhenRemoveRole(roleId);
            sysRoleService.removeById(roleId);
        }
        return ResponseResult.success();
    }

    /**
     * Description 查询所有数据
     * @author : jingwen
     * Date 2022-08-31 11:17:21
     **/
    @GetMapping("/findAll")
    public ResponseResult<List<SysRole>> findAll() {
        return ResponseResult.success(sysRoleService.list());
    }

    /**
     * Description 分页查询
     * @author : jingwen
     * Date 2022-08-31 11:17:21
     **/
    @PreAuthorize("hasAuthority('system:sysRole:query')")
    @PostMapping("/getPageList")
    public ResponseResult<MyPageVO<SysRole>> getPageList(@RequestBody QuerySysRoleDTO querySysRoleDTO) {
        System.out.println(querySysRoleDTO);
        return ResponseResult.success(sysRoleService.getRolePageList(querySysRoleDTO));
    }

    /**
     * Description 根据角色id查询分配的菜单
     * @author : jingwen
     * Date 2022/8/22 19:43
     **/
    @PreAuthorize("hasAuthority('system:sysRole:permission')")
    @GetMapping("/getRoleMenu")
    public ResponseResult<List<Integer>> getRoleMenu(@RequestParam Integer roleId){
        return ResponseResult.success(sysRoleMenuService.getMenuIdsByRoleId(roleId));
    }


    /**
     * Description 绑定角色id和菜单关系
     * @author : jingwen
     * Date 2022/8/22 19:43
     **/
    @PreAuthorize("hasAuthority('system:sysRole:permission')")
    @SysLog(logModule= LogModuleConst.SysRoleModule, logType = LogTypeConst.UPDATE, logDesc = "更新角色菜单")
    @PostMapping("/updateRoleMenu")
    public ResponseResult<Object> updateRoleMenu(@RequestBody SysRoleMenuDTO sysRoleMenuDTO){
        /*
          先删除再增加
         */
        sysRoleMenuService.updateRoleMenu(sysRoleMenuDTO.getRoleId(), sysRoleMenuDTO.getMenuIds());
        return ResponseResult.success();
    }

    /**
     * Description: 内部调用——获取用户角色名
     * @author : jingwen
     * Date: 2023/4/24 20:20
     **/
    @GetMapping("/inner/getRoleNameListByUserId")
    public ResponseResult<List<String>> getRoleNameListByUserId(@RequestParam("userId") Integer userId){
        return ResponseResult.success(sysRoleService.getRoleNameListByUserId(userId));
    }



}

