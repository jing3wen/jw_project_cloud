package com.jw_server.system.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw_server.system.api.dao.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * Description 系统用户表 Mapper 接口
 * Author jingwen
 * Date 2022-08-29 16:21:58
 **/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * Description: 根据用户Id查询密码
     * Author: jingwen
     * Date: 2022/9/10 20:28
     **/
    String selectPasswordByUserId(Integer userId);

    /**
     * Description: 更新用户密码
     * Author: jingwen
     * Date: 2022/9/10 20:30
     **/
    void updatePasswordByUserId(@Param("userId") Integer userId,
                                @Param("password") String password);

}
