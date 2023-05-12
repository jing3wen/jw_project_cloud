package com.jw_server.common.base.core.constants;

/**
 * Description 返回状态字
 * @author : jingwen
 * DATE 2022/8/17 10:23
 */
public class ResponseCode {

    public static Integer CODE_200 = 200; //成功
    public static Integer CODE_202 = 202; //一个请求已经进入后台排队（异步任务）
    public static Integer CODE_401 = 401;  // 身份认证验证
    public static Integer CODE_403 = 403;  // 禁止访问, 登录认证使用
    public static Integer CODE_400 = 400;  // 参数错误
    public static Integer CODE_500 = 500; // 系统错误
    public static Integer CODE_600 = 600; // 其他业务异常
}
