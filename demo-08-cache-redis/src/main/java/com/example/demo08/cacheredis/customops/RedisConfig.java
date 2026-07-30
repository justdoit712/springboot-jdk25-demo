package com.example.demo08.cacheredis.customops;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 * 自定义 Redis 配置
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
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
