package com.example.demo05.mybatis.entity;

/**
 * <p>
 * 用户实体类 (纯原生对象，没有任何 MyBatis-Plus 注解)
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:18
 */
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
