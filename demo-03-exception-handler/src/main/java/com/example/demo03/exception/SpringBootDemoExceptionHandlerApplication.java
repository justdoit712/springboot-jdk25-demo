package com.example.demo03.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 全局异常处理启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
@SpringBootApplication
public class SpringBootDemoExceptionHandlerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoExceptionHandlerApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8082");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application ExceptionHandler Demo started successfully!\n\t" +
                "Access URL: \thttp://localhost:" + port + path + "/test/success\n" +
                "----------------------------------------------------------");
    }
}
