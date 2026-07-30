package com.example.demo08.cacheredis.nativeops;

import com.example.demo08.cacheredis.customops.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 原生交互控制器
 * 演示如果不做任何配置，Spring Boot 默认的 RedisTemplate 是如何工作的
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 19:24
 */
@RestController
@RequestMapping("/redis/native")
public class NativeController {

    // 自动注入 Spring Boot 默认提供的 RedisTemplate (使用 JDK 序列化)
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @GetMapping("/set-string")
    public String setString(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return "【原生】存储 String 成功！去查看 Redis 会发现 Key 带有类似 \\xac\\xed\\x00 的乱码前缀。";
    }

    @PostMapping("/set-user")
    public String setUser(@RequestBody User user) {
        redisTemplate.opsForValue().set("native:user:" + user.getId(), user);
        return "【原生】存储 Object 成功！去查看 Redis 会发现 Value 是完整的 JDK 序列化乱码。";
    }
}
