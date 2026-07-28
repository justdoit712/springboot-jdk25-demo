package com.example.demo04.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Actuator 监控测试启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 13:45
 */
@SpringBootApplication
public class SpringBootDemoActuatorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoActuatorApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8083");
        String path = env.getProperty("server.servlet.context-path", "");
        String actuatorPort = env.getProperty("management.server.port", port);

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Actuator Demo started successfully!\n\t" +
                "Business URL: \thttp://localhost:" + port + path + "\n\t" +
                "Actuator URL: \thttp://localhost:" + actuatorPort + "/actuator\n" +
                "----------------------------------------------------------");
    }
}
