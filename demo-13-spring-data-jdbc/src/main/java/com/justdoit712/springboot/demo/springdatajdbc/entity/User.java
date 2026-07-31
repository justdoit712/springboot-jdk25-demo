package com.justdoit712.springboot.demo.springdatajdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

/**
 * <p>
 * 用户实体类
 * 注意这里使用的是 Spring Data 自己的 @Table 和 @Id 注解，
 * 没有任何 javax.persistence 相关的 JPA/Hibernate 依赖，极其纯净。
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 14:09
 */
@Table("USERS")
public class User {

    @Id
    private Long id;

    private String name;

    private Integer age;

    private Integer status;

    private Date createTime;

    // Getters and Setters
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
