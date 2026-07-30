package com.example.demo08.cacheredis.customops;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 * 自定义 Redis 配置类
 * 
 * 核心目的：
 * 解决 Spring Boot 默认的 RedisTemplate 采用 JDK 序列化（JdkSerializationRedisSerializer）
 * 导致存入 Redis 的数据产生类似 "\xac\xed\x00\x05t\x00" 二进制乱码前缀的问题。
 * 
 * 通过自定义配置，我们给 Redis 替换了更优秀的"翻译官"：
 * 1. StringRedisSerializer：负责将 Redis 的 Key 序列化为干净清爽的普通字符串。
 * 2. GenericJackson2JsonRedisSerializer：负责将 Java 对象序列化为跨语言通用的 JSON 字符串保存到 Value 中。
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 19:24
 */
@Configuration
public class RedisConfig {

    /**
     * 注意：为了让您能同时对比两种效果，我们将这里的 Bean 命名为 "customRedisTemplate"。
     * 这样它就不会覆盖 Spring Boot 自动装配的默认 "redisTemplate"，两个模板可以共存。
     */
    @Bean(name = "customRedisTemplate")
    public RedisTemplate<String, Object> customRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 1. 绑定连接工厂 (让这个模板知道通过哪个底层客户端去连 Redis)
        template.setConnectionFactory(factory);

        // 2. 为外层普通的 Key 设置序列化器：指定为纯字符串，防止出现乱码前缀
        template.setKeySerializer(new StringRedisSerializer());
        // 3. 为外层普通的 Value 设置序列化器：指定为 JSON，把 Java 对象变成跨语言通用的 JSON 字符串
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // 4. 为 Hash 结构内部的 Key 设置序列化器
        template.setHashKeySerializer(new StringRedisSerializer());
        // 5. 为 Hash 结构内部的 Value 设置序列化器
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 6. 必须执行这一句：让模板做一下初始化自检，确保所有必需的属性（如序列化器）都已设置完毕
        template.afterPropertiesSet();
        return template;
    }
}
