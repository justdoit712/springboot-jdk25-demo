package com.example.demo01.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Hello 测试接口
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 11:30
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello, Spring Boot 3 & JDK 25!";
    }
}
