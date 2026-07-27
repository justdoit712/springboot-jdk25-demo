package com.example.demo02.properties.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * <p>
 * 开发者属性绑定类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 11:45
 */
@Component
@ConfigurationProperties(prefix = "developer")
public class DeveloperProperty {

    private String name;
    private String website;
    private Integer age;
    private List<String> skills;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
