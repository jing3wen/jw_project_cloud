package com.jw_server.system.service;

import com.jw_server.system.api.client.SysLogFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author : jingwen
 * @Description :
 * @DATE : 2023/4/24 18:58
 */
@Slf4j
@SpringBootApplication
@EnableWebMvc
@EnableAsync  //开启异步任务
@EnableFeignClients(basePackageClasses = {SysLogFeignClient.class})
public class SystemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemServiceApplication.class, args);
        log.info("SystemServiceApplication——启动成功");
    }
}
