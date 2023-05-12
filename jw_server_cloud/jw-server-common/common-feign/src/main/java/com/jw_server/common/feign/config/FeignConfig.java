package com.jw_server.common.feign.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jw_server.common.base.core.constants.BaseConfigConst;
import com.jw_server.common.base.core.constants.FeignInnerConst;
import com.jw_server.common.base.core.constants.SecurityContextConst;
import com.jw_server.common.base.core.context.MySecurityContext;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Description: openfeign的配置类，
 * feign调用内部接口时，会将请求header中的所有内容复制一份，header不更大了吗？
 * @author : jingwen
 * DATE: 2023/4/23 17:15
 */
@Slf4j
@Configuration
public class FeignConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 让DispatcherServlet向子线程传递RequestContext
     * @param servlet servlet
     * @return 注册bean
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * 覆写拦截器，在feign发送请求前取出原来的header并转发
     *
     * @return 拦截器
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            //获取请求头
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                //内部调用时将token信息写入到请求header
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if(name.equals(BaseConfigConst.TOKEN_HEADER)){
                        //将请求头保存到模板中
                        template.header(name, values);
                        break;
                    }
                }
                //内部调用时通过header字段标注为内部调用
                template.header(FeignInnerConst.HEADER_INNER_NAME, FeignInnerConst.HEADER_INNER_VALUE);
                log.info("当前服务名称: "+applicationName+", 内部调用: "+template.url());
                template.header("serviceName",applicationName);
            }

        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


}
