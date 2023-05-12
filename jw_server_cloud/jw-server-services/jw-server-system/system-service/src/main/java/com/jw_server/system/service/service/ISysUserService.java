package com.jw_server.system.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.mybatis.page.MyPageVO;
import com.jw_server.system.api.dao.entity.SysUser;
import com.jw_server.system.service.dao.dto.QuerySysUserDTO;
import com.jw_server.system.service.dao.dto.RegisterUserDTO;
import com.jw_server.system.service.dao.dto.ResetPasswordDTO;
import com.jw_server.system.service.dao.dto.SysForgetPasswordOrUpdateBindDTO;
import com.jw_server.system.service.dao.vo.SysUserVO;

import java.util.List;


/**
 * Description 系统用户表 服务类
 * Author jingwen
 * Date 2022-08-29 16:21:58
 **/
public interface ISysUserService extends IService<SysUser> {


    /**
     * @Description 用户注册服务
     * @Author jingwen
     * @Date 2022/8/20 10:42
     **/
    void register(RegisterUserDTO registerUserDTO);

    /**
     * @Description 根据用户名查询用户信息服务
     * @Author jingwen
     * @Date 2022/8/20 10:42
     **/
    SysUser getUserByUserName(String username);


    /**
     * Description: 用户列表分页
     * Author: jingwen
     * Date: 2022/9/2 16:40
     **/
    MyPageVO<SysUserVO> getUserPageList(QuerySysUserDTO querySysUserDTO);


    /**
     * Description: 重置用户密码
     * Author: jingwen
     * Date: 2022/9/10 20:20
     **/
    void resetPassword(ResetPasswordDTO resetPasswordDTO);


    /**
     * Description: 获取验证码
     * Author: jingwen
     * Date: 2023/2/25 19:28
     **/
    void getCodeForType(String email, String phone, String type);

    /**
     * Description: 找回密码
     * Author: jingwen
     * Date: 2023/2/25 23:34
     **/
    void updateForgetPassword(SysForgetPasswordOrUpdateBindDTO forgetPasswordDTO);

    /**
     * Description: 根据密码 绑定邮箱/手机号 或 更改绑定邮箱/手机号
     * Author: jingwen
     * Date: 2023/2/26 13:59
     **/
    void updateBindByPassword(SysForgetPasswordOrUpdateBindDTO updateBindDTO);

    /**
     * Description: 批量删除用户, 用户绑定的属性太多, 此处使用假删除
     * Author: jingwen
     * Date: 2023/3/1 15:24
     **/
    void deleteUserBatch(List<Integer> ids);

    /**
     * Description: 内部调用——异步更新用户最近登录时间
     * Author: jingwen
     * Date: 2022/9/10 18:05
     **/
    public void asyncUpdateUserLastLoginTime(Integer userId);
    /**
     * Description: 内部调用——根据用户名查询用户信息
     * @author : jingwen
     * Date: 2023/4/24 19:42
     **/
    SysUser getAuthUserByUsername(String username);
}
