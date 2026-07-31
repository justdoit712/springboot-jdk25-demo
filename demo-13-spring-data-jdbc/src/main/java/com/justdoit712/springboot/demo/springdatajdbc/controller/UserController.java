package com.justdoit712.springboot.demo.springdatajdbc.controller;

import com.justdoit712.springboot.demo.springdatajdbc.entity.User;
import com.justdoit712.springboot.demo.springdatajdbc.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户测试接口
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 14:10
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        // 自带的方法
        return userRepository.findAll();
    }

    @GetMapping("/name/{name}")
    public Iterable<User> getUsersByName(@PathVariable String name) {
        // 演示通过方法名自动生成的 SQL
        return userRepository.findByName(name);
    }

    @GetMapping("/complex")
    public Iterable<User> getComplexUsers(@RequestParam Integer status, @RequestParam Integer minAge) {
        // 演示强行手写原生 SQL
        return userRepository.findByCustomComplexCondition(status, minAge);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // 自带的方法，注意没有复杂的状态管理，直接一条 INSERT 执行过去
        return userRepository.save(user);
    }
}
