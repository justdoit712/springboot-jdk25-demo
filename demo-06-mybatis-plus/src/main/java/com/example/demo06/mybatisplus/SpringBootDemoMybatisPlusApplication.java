package com.example.demo06.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * MyBatis-Plus 测试启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:12
 */
@SpringBootApplication
@MapperScan("com.example.demo06.mybatisplus.mapper") // 扫描 Mapper 接口
public class SpringBootDemoMybatisPlusApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoMybatisPlusApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8087");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application MyBatis-Plus Demo started successfully!\n\t" +
                "Test URL: \thttp://localhost:" + port + path + "/user/list\n" +
                "----------------------------------------------------------");
    }
}
