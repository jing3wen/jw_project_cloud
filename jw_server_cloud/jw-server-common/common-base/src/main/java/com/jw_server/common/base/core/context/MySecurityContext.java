package com.jw_server.common.base.core.context;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.jw_server.common.base.core.constants.SecurityContextConst;
import com.jw_server.common.base.dao.LoginUserVO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description : 为什么要写这个ThreadLocal存储认证上下文信息,
 * 因为spring security太重量级, 一旦引入模块就自动拦截, 搬来SpringSecurity就可以存储认证信息, 这个地方写一个感觉是脱裤子放屁了
 * @author : jingwen
 * DATE : 2023/4/30 23:38
 */
public class MySecurityContext {

    private static TransmittableThreadLocal<Map<String, Object>> transmittableThreadLocal  = new TransmittableThreadLocal<>();

    public static Map<String, Object> getLocalMap()
    {
        Map<String, Object> map = transmittableThreadLocal.get();
        if (map == null)
        {
            map = new ConcurrentHashMap<String, Object>();
            transmittableThreadLocal.set(map);
        }
        return map;
    }

    public static void set(String key, Object value)
    {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value);
    }



    public static Object get(String key)
    {
        Map<String, Object> map = getLocalMap();
        return map.getOrDefault(key, null);
    }

    public static void setLoginUserVO(LoginUserVO loginUserVO){
        set(SecurityContextConst.LOGIN_USER_VO, loginUserVO);
    }

    public static LoginUserVO getLoginUserVO(){
        Object object = get(SecurityContextConst.LOGIN_USER_VO);
        if(ObjectUtil.isNotEmpty(object)){
            return (LoginUserVO) object;
        }else {
            return null;
        }
    }

    /**
     * Description: 获取当前认证用户的用户名
     * @author : jingwen
     * Date: 2023/5/1 9:25
     * @return : 若认证用户存在, 返回用户名; 若无认证用户, 返回匿名anonymousUser
     *
     **/
    public static String getLoginUserUsername(){
        LoginUserVO loginUserVO = (LoginUserVO) get(SecurityContextConst.LOGIN_USER_VO);
        if(ObjectUtil.isNotEmpty(loginUserVO)){
            return loginUserVO.getUsername();
        }else {
            return SecurityContextConst.ANONYMOUS_USER;
        }
    }


}
