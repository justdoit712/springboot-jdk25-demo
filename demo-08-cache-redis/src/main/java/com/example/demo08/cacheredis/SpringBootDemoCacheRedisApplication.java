package com.example.demo08.cacheredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Redis 缓存测试启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 18:55
 */
@SpringBootApplication
public class SpringBootDemoCacheRedisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoCacheRedisApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8089");
        String path = env.getProperty("server.servlet.context-path", "");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application Redis Cache Demo started successfully!\n\t" +
                "Manual Set Test: \thttp://localhost:" + port + path + "/redis/set?key=hello&value=world\n\t" +
                "Cacheable Test: \thttp://localhost:" + port + path + "/redis/user/1\n" +
                "----------------------------------------------------------");
    }
}
