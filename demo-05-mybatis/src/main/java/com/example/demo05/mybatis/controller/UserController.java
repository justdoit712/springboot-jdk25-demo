package com.example.demo05.mybatis.controller;

import com.example.demo05.mybatis.entity.User;
import com.example.demo05.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 测试基于注解的 SQL
     */
    @GetMapping("/annotation-list")
    public List<User> getListByAnnotation() {
        return userMapper.findAllAnnotations();
    }

    /**
     * 测试基于 XML 映射的 SQL
     */
    @GetMapping("/xml-get/{id}")
    public User getByIdXml(@PathVariable("id") Long id) {
        return userMapper.findByIdXml(id);
    }
}
