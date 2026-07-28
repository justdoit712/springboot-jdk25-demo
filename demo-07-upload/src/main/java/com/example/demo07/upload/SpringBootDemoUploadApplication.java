package com.example.demo07.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 文件上传测试启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:38
 */
@SpringBootApplication
public class SpringBootDemoUploadApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoUploadApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8088");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Upload Demo started successfully!\n\t" +
                "Test Page: \thttp://localhost:" + port + path + "/index.html\n" +
                "----------------------------------------------------------");
    }
}
