package com.jw_server.system.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.exception.ServiceException;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.service.dao.dto.QuerySysRoleDTO;
import com.jw_server.system.service.dao.entity.SysRole;
import com.jw_server.system.service.dao.mapper.SysRoleMapper;
import com.jw_server.system.service.dao.mapper.SysRoleMenuMapper;
import com.jw_server.system.service.dao.mapper.SysUserRoleMapper;
import com.jw_server.system.service.service.ISysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description 系统角色表 服务实现类
 * Author jingwen
 * Date 2022-08-31 11:17:21
 **/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public MyPageVO<SysRole> getRolePageList(QuerySysRoleDTO querySysRoleDTO) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(querySysRoleDTO.getRoleCode())){
            queryWrapper.like(SysRole::getRoleCode, querySysRoleDTO.getRoleCode());
        }
        if(StrUtil.isNotEmpty(querySysRoleDTO.getRoleName())){
            queryWrapper.like(SysRole::getRoleName, querySysRoleDTO.getRoleName());
        }
        return new MyPageVO<>(
                page(new Page<>(querySysRoleDTO.getPageNum(),
                        querySysRoleDTO.getPageSize()),
                        queryWrapper)
        );
    }

    @Override
    public void deleteUserRoleAndRoleMenuWhenRemoveRole(Integer roleId) {
        //根据角色id删除所有绑定的用户
        sysUserRoleMapper.deleteUserRoleByRoleId(roleId);
        //删除角色所有绑定的菜单信息
        sysRoleMenuMapper.deleteRoleMenuByRoleId(roleId);
    }

    @Override
    public List<SysRole> selectAllRoleByUserId(Integer userId) {
        return sysRoleMapper.selectAllRoleByUserId(userId);
    }

    @Override
    public void addOrUpdateSysRole(SysRole sysRole) {
        //查询是否存在相同角色编码 (不用查询所有数据，查主键id就行了)
        SysRole findSameRoleCode = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .select(SysRole::getId)
                .eq(SysRole::getRoleCode, sysRole.getRoleCode()));

        if(ObjectUtil.isNotEmpty(findSameRoleCode) && !(findSameRoleCode.getId().equals(sysRole.getId()))){
            throw new ServiceException(ResponseCode.CODE_400,"存在相同标识符角色，请更改唯一标识符");
        }

        //查询是否存在相同角色名 (不用查询所有数据，查主键id就行了)
        SysRole findSameRoleName = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .select(SysRole::getId)
                .eq(SysRole::getRoleName, sysRole.getRoleName()));
        if(ObjectUtil.isNotEmpty(findSameRoleName) && !(findSameRoleName.getId().equals(sysRole.getId()))){
            throw new ServiceException(ResponseCode.CODE_400,"存在相同角色名，请更改角色名");
        }
        saveOrUpdate(sysRole);
    }

    /**
     * Description: 内部模块获取用户角色名
     * @author : jingwen
     * Date: 2023/4/24 20:18
     **/
    @Override
    public List<String> getRoleNameListByUserId(Integer userId) {
        return sysRoleMapper.selectRoleNameListByUserId(userId);
    }
}
