package com.example.demo08.cacheredis.service;

import com.example.demo08.cacheredis.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户服务类（模拟数据库操作，并结合 Spring Cache 注解演示缓存管理）
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 18:55
 */
@Service
public class UserService {

    // 模拟数据库
    private static final Map<Long, User> DATABASES = new HashMap<>();

    static {
        DATABASES.put(1L, new User(1L, "justdoit712"));
        DATABASES.put(2L, new User(2L, "Spring Boot 3"));
    }

    /**
     * @Cacheable 触发缓存逻辑：
     * 如果缓存(userCache)中存在指定 key (id) 的数据，则直接返回，不会执行方法体；
     * 如果缓存中没有，则执行方法体，并将返回结果存入缓存。
     */
    @Cacheable(value = "userCache", key = "#id")
    public User getUser(Long id) {
        System.out.println("⚠️ 缓存未命中，正在进入数据库查询用户 ID: " + id);
        return DATABASES.get(id);
    }

    /**
     * @CachePut 触发缓存更新逻辑：
     * 每次都会执行方法体，并将返回结果覆盖存入指定的缓存中。
     * 通常用于更新数据的接口。
     */
    @CachePut(value = "userCache", key = "#user.id")
    public User updateUser(User user) {
        System.out.println("⚠️ 正在更新数据库中的用户: " + user.getId());
        DATABASES.put(user.getId(), user);
        return user;
    }

    /**
     * @CacheEvict 触发缓存清除逻辑：
     * 执行完方法体后，会删除缓存中指定 key 的数据。
     * 通常用于删除数据的接口。
     */
    @CacheEvict(value = "userCache", key = "#id")
    public void deleteUser(Long id) {
        System.out.println("⚠️ 正在从数据库中删除用户 ID: " + id);
        DATABASES.remove(id);
    }
}
