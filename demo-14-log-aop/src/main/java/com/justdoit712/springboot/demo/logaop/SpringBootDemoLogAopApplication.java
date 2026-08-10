package com.justdoit712.springboot.demo.logaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AOP 日志切面启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 15:47
 */
@SpringBootApplication
public class SpringBootDemoLogAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoLogAopApplication.class, args);
        
        System.out.println("\n==========================================================");
        System.out.println("🎉 AOP 切面日志服务启动成功！");
        System.out.println("==========================================================");
        System.out.println("👉 请点击以下链接测试接口（注意观察控制台打印的精美日志）：");
        System.out.println("   1. 测试普通 GET 请求: http://localhost:8080/api/test/hello?name=Antigravity");
        System.out.println("   2. 测试异常请求 (会抛出错误): http://localhost:8080/api/test/error");
        System.out.println("==========================================================\n");
    }
}
