package com.example.demo08.cacheredis.customops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 自定义配置交互控制器
 * 演示经过 JSON 序列化器包装后的 RedisTemplate 是如何工作的
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 19:24
 */
@RestController
@RequestMapping("/redis/custom")
public class CustomController {

    // 使用 @Qualifier 明确指定注入我们在 RedisConfig 中配的 "customRedisTemplate"
    @Autowired
    @Qualifier("customRedisTemplate")
    private RedisTemplate<String, Object> customRedisTemplate;

    @GetMapping("/set-string")
    public String setString(@RequestParam String key, @RequestParam String value) {
        customRedisTemplate.opsForValue().set(key, value);
        return "【自定义】存储 String 成功！去查看 Redis 会发现 Key 没有任何乱码。";
    }

    @PostMapping("/set-user")
    public String setUser(@RequestBody User user) {
        customRedisTemplate.opsForValue().set("custom:user:" + user.getId(), user);
        return "【自定义】存储 Object 成功！去查看 Redis 看看是不是漂亮的 JSON 格式？";
    }
}
