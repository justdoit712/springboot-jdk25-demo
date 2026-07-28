package com.example.demo06.mybatisplus.controller;

import com.example.demo06.mybatisplus.entity.User;
import com.example.demo06.mybatisplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-28 14:12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.list();
    }

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getById(id);
    }
}
