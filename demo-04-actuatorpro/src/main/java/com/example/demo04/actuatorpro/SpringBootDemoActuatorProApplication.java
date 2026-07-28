package com.example.demo04.actuatorpro;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Spring Boot Admin (Actuator Pro) 启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:02
 */
@EnableAdminServer
@SpringBootApplication
public class SpringBootDemoActuatorProApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoActuatorProApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8085");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Actuator Pro (Admin) started successfully!\n\t" +
                "Access URL: \thttp://localhost:" + port + path + "\n" +
                "----------------------------------------------------------");
    }
}
