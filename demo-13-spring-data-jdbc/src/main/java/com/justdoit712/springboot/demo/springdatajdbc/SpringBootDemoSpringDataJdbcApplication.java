package com.justdoit712.springboot.demo.springdatajdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * Spring Data JDBC 启动类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 14:09
 */
@SpringBootApplication
public class SpringBootDemoSpringDataJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoSpringDataJdbcApplication.class, args);
        
        System.out.println("\n==========================================================");
        System.out.println("🎉 项目启动成功！请直接点击以下链接进行测试：");
        System.out.println("==========================================================");
        System.out.println("👉 1. H2 数据库网页控制台:");
        System.out.println("   URL: http://localhost:8080/h2-console");
        System.out.println("   (登录提示: JDBC URL 必须填 jdbc:h2:mem:testdb，User Name 填 sa，密码留空)");
        System.out.println();
        System.out.println("👉 2. 测试接口 1：获取全部用户 (自带 findAll 方法)");
        System.out.println("   http://localhost:8080/api/users/all");
        System.out.println();
        System.out.println("👉 3. 测试接口 2：按名字查询 (根据方法名 findByName 自动生成 SQL)");
        System.out.println("   http://localhost:8080/api/users/name/Alice");
        System.out.println();
        System.out.println("👉 4. 测试接口 3：复杂条件查询 (通过 @Query 强行手写原生 SQL)");
        System.out.println("   http://localhost:8080/api/users/complex?status=1&minAge=20");
        System.out.println("==========================================================\n");
    }   
}
