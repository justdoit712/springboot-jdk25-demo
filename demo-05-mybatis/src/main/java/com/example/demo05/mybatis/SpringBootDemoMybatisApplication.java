package com.example.demo05.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 原生 MyBatis 测试启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:18
 */
@SpringBootApplication
@MapperScan("com.example.demo05.mybatis.mapper")
public class SpringBootDemoMybatisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoMybatisApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8086");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Native MyBatis Demo started successfully!\n\t" +
                "Annotation URL: \thttp://localhost:" + port + path + "/user/annotation-list\n\t" +
                "XML URL: \t\thttp://localhost:" + port + path + "/user/xml-get/1\n" +
                "----------------------------------------------------------");
    }
}
