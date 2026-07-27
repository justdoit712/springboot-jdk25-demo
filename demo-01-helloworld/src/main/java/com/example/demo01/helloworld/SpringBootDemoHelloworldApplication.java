package com.example.demo01.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Hello World 启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 11:30
 */
@SpringBootApplication
public class SpringBootDemoHelloworldApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoHelloworldApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Helloworld started successfully!\n\t" +
                "Access URL: \thttp://localhost:" + port + path + "/hello\n" +
                "----------------------------------------------------------");
    }
}
