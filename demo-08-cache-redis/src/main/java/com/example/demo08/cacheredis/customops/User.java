package com.example.demo08.cacheredis.customops;

import java.io.Serializable;

/**
 * <p>
 * 用户实体类 (实现 Serializable 是为了允许原生 JDK 序列化)
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-30 19:22
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
