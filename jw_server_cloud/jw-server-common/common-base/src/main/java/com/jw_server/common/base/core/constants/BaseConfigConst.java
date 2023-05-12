package com.jw_server.common.base.core.constants;

/**
 * Description : 一些基础配置常量
 *
 * @author : jingwen
 * Date : 2023/5/12 20:12
 */
public class BaseConfigConst {

    /**
     * jwt储存的请求头token固定写法
     */
    public static final String TOKEN_HEADER = "token";

    /**
     * jwt有效时间 (60*60*1000) 一个小时
     */
    public static final int TOKEN_TTL = 3600000;

    /**
     * jwt加密明文
     */
    public static final String TOKEN_KEY = "jingwen";
}
