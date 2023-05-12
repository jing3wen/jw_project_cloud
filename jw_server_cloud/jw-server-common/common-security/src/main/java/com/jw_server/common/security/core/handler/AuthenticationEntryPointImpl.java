package com.jw_server.common.security.core.handler;

import com.alibaba.fastjson.JSON;
import com.jw_server.common.base.core.constants.ResponseCode;
import com.jw_server.common.base.result.ResponseEnum;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.base.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 自定义认证失败处理，此处没必要写，因为我已经在userLogin中提前捕获了异常
 * Author: jingwen
 * DATE: 2022/8/30 20:48
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult<String> result = ResponseResult.error(ResponseEnum.USER_UNAUTHORIZED);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
