package com.jw_server.blog.service;

import com.jw_server.system.api.client.SysLogFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Description :
 *
 * @author : jingwen
 * Date : 2023/5/1 18:37
 */
@Slf4j
@SpringBootApplication
@EnableWebMvc
@EnableAsync  //开启异步任务
@EnableFeignClients(basePackageClasses = {SysLogFeignClient.class})
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        log.info("BlogApplication——启动成功");
    }
}
