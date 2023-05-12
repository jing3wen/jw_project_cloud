package com.jw_server.common.base.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 认证用户，专门写入到认证上下文中
 * @author : jingwen
 * DATE: 2022/8/31 14:43
 *
 *
 * 注意！！！
 * Date: 2023/2/6 16:50 修改：
 * 为了照顾博客子系统的配置，增添一些属性 (邮箱,电话,性别,备注)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO implements Serializable {

    private Integer id;

    private String username;

    private String nickname;

    private String avatar;

    private String token;

    private List<String> roleList;

    // permissions只有权限按钮，没有权限菜单
    private List<String> permissionList;

    //下列为新增属性
    private String email;

    private String phone;

    private String sex;

    private String remark;


}
