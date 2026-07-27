package com.example.demo02.properties.controller;

import com.example.demo02.properties.property.DeveloperProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 属性测试接口
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 11:45
 */
@RestController
@RequestMapping("/property")
public class PropertyController {

    // ==========================================
    // 1. 单个属性注入 (@Value)
    // ==========================================
    @Value("${server.port}")
    private String serverPort;

    // ==========================================
    // 2. 字段注入 / 普通注入 (Field Injection)
    // 优点：代码简洁
    // 缺点：不推荐，无法声明为 final，不利于单元测试
    // ==========================================
    @Autowired
    private DeveloperProperty fieldInjectedProperty;

    // ==========================================
    // 3. 构造器注入 (Constructor Injection)
    // 优点：Spring 官方推荐，可声明为 final，保证对象完全初始化和不可变性，方便测试
    // ==========================================
    private final DeveloperProperty constructorInjectedProperty;

    @Autowired // 如果类只有一个构造函数，此注解可省略
    public PropertyController(DeveloperProperty constructorInjectedProperty) {
        this.constructorInjectedProperty = constructorInjectedProperty;
    }

    @GetMapping
    public Map<String, Object> getProperties() {
        Map<String, Object> result = new HashMap<>();
        // 演示单个属性注入结果
        result.put("1_serverPortFromValue", serverPort);
        // 演示字段注入结果
        result.put("2_fieldInjectedProperty", fieldInjectedProperty);
        // 演示构造器注入结果
        result.put("3_constructorInjectedProperty", constructorInjectedProperty);
        return result;
    }
}
