package com.jw_server.common.security.core.handler;

import com.alibaba.fastjson.JSON;
import com.jw_server.common.base.result.ResponseEnum;
import com.jw_server.common.base.result.ResponseResult;
import com.jw_server.common.base.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 自定义授权失败处理
 * Author: jingwen
 * DATE: 2022/8/30 20:50
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult<String> result = new ResponseResult<>(ResponseEnum.USER_ACCESS_FORBIDDEN);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
