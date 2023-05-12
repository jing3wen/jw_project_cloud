package com.jw_server.system.service.core.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 系统常量
 * Author: jingwen
 * DATE: 2023/2/18 23:51
 */
public class SysConst {

    /**
     * 用户管理
     */
    public static final String SysUserModule = "用户管理";

    /**
     * 角色管理
     */
    public static final String SysRoleModule = "角色管理";

    /**
     * 菜单管理
     */
    public static final String SysMenuModule = "菜单管理";


    /**
     * spring security 匿名用户
     **/
    public static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * 用户停用状态
     **/
    public static final String USER_FORBID_STATUS = "0";

    /**
     * 允许用户注册的类型: 博客用户，演示用户
     * 注意该数据要和数据库的sys_role表的role_code对应
     **/
    public static final List<String> REGISTER_USER_TYPE_LIST = Arrays.asList("BLOG_REGISTER_ROLE", "SHOW_ROLE");


    /**
     * 用户注册
     **/
    public static final String REGISTER_USER = "register_user";
    /**
     * 用户注册验证码缓存
     **/
    public static final String REGISTER_USER_CODE_CACHE = "sys:register_user_code_cache";

    /**
     * 用户找回密码
     **/
    public static final String USER_FORGET_PASSWORD = "user_forget_password";
    /**
     * 用户找回密码验证码缓存
     **/
    public static final String USER_FORGET_PASSWORD_CODE_CACHE = "sys:user_forget_password_code_cache";

    /**
     * 更改绑定邮箱/手机号
     **/
    public static final String UPDATE_USER_BIND = "update_user_bind";

    /**
     * 更改绑定邮箱/手机号 验证码缓存
     **/
    public static final String UPDATE_USER_BIND_CODE_CACHE = "update_user_bind_code_cache";

}
