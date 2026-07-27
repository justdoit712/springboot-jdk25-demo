package com.example.demo02.properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 属性绑定启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 11:45
 */
@SpringBootApplication
public class SpringBootDemoPropertiesApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoPropertiesApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8081");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Properties Demo started successfully!\n\t" +
                "Access URL: \thttp://localhost:" + port + path + "/property\n" +
                "----------------------------------------------------------");
    }
}
