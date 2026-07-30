package com.example.demo08.cacheredis.entity;

import java.io.Serializable;

/**
 * <p>
 * 用户实体类 (实现 Serializable 是为了允许对象在网络中传输以及保存在 Redis 缓存中)
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 18:55
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
