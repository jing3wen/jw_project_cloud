package com.jw_server.file.service;

import com.jw_server.system.api.client.SysLogFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Description :
 *
 * @author : jingwen
 * DATE : 2023/5/1 14:17
 */
@Slf4j
@SpringBootApplication
@EnableWebMvc
@EnableFeignClients(basePackageClasses = {SysLogFeignClient.class})
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
        log.info("FileApplication——启动成功");
    }
}
