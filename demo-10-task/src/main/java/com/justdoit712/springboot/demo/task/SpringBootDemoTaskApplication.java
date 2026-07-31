package com.justdoit712.springboot.demo.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * Spring Boot 基础定时任务启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:38
 */
@SpringBootApplication
@EnableScheduling // 必须加上这个注解才能开启定时任务
public class SpringBootDemoTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoTaskApplication.class, args);
    }
}
