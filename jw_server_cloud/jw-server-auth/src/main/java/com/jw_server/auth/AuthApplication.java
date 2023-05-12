package com.jw_server.auth;

import com.jw_server.system.api.client.SysLogFeignClient;
import com.jw_server.system.api.client.SysUserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Description: Auth模块启动类
 * @author : jingwen
 * DATE: 2023/4/23 17:30
 */
@Slf4j
@EnableWebMvc
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {SysUserFeignClient.class, SysLogFeignClient.class})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("AuthApplication——启动成功");
    }
}
