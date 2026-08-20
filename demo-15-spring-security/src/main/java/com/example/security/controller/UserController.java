package com.example.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * User Controller for protected resources
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public Map<String, Object> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Map.of(
                "code", 200,
                "message", "Success",
                "data", Map.of(
                        "username", username,
                        "roles", new String[]{"ROLE_ADMIN"},
                        "avatar", "https://api.dicebear.com/7.x/adventurer/svg?seed=" + username
                )
        );
    }
}
