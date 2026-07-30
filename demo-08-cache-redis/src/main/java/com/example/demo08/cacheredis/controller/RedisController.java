package com.example.demo08.cacheredis.controller;

import com.example.demo08.cacheredis.entity.User;
import com.example.demo08.cacheredis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * Redis 测试控制器
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 18:55
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserService userService;

    @Autowired
    public RedisController(StringRedisTemplate stringRedisTemplate, UserService userService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
    }

    /**
     * 1. 演示最基础的 RedisTemplate API 手动操作
     */
    @GetMapping("/set")
    public String setKey(@RequestParam String key, @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "手动设置 Redis 成功！Key: " + key + ", Value: " + value;
    }

    @GetMapping("/get")
    public String getKey(@RequestParam String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return "手动获取 Redis 结果：" + value;
    }

    /**
     * 2. 演示基于注解的 Spring Cache (@Cacheable)
     */
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        // 第一次查询会打印"缓存未命中"，第二次查询会直接从 Redis 取，不会打印日志
        return userService.getUser(id);
    }

    /**
     * 3. 演示基于注解的 Spring Cache 更新 (@CachePut)
     */
    @PostMapping("/user/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 4. 演示基于注解的 Spring Cache 删除 (@CacheEvict)
     */
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "用户删除成功，缓存已同步清理！";
    }
}
