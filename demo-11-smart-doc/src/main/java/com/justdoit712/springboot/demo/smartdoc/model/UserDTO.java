package com.justdoit712.springboot.demo.smartdoc.model;

/**
 * <p>
 * 用户信息传输对象
 * 注意观察：这里面没有任何 Swagger 的 @ApiModelProperty 注解
 * 全靠标准的 Javadoc 来生成文档
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:47
 */
public class UserDTO {

    /**
     * 用户 ID (唯一主键)
     * @mock 10001
     */
    private Long id;

    /**
     * 用户姓名
     * @required
     * @mock 张三
     */
    private String name;

    /**
     * 用户年龄
     * @mock 25
     */
    private Integer age;

    /**
     * 用户邮箱地址
     * @mock zhangsan@example.com
     */
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
