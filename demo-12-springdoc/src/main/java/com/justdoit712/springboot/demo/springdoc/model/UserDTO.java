package com.justdoit712.springboot.demo.springdoc.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 用户信息传输对象
 * 注意：OpenAPI 3 规范中，使用 @Schema 替代了原来的 @ApiModel 和 @ApiModelProperty
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 11:52
 */
@Schema(description = "用户实体信息")
public class UserDTO {

    @Schema(description = "用户 ID (唯一主键)", example = "10001")
    private Long id;

    @Schema(description = "用户姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String name;

    @Schema(description = "用户年龄", example = "25")
    private Integer age;

    @Schema(description = "用户邮箱地址", example = "zhangsan@example.com")
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
