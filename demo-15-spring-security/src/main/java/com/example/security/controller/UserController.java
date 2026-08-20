package com.example.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 用户业务控制器（受保护资源接口）
 * </p>
 * <p>
 * <b>核心职责与设计要点：</b>
 * <ol>
 *   <li><b>受保护端点：</b> 挂载在 {@code /api/user/**} 下的所有接口均受 Spring Security 保护（需 {@code authenticated()}）。
 *       未登录或携带非法/过期 Token 的请求将在过滤链末端被拦截并直接返回 401。</li>
 *   <li><b>上下文身份消费：</b> 控制器内部无需解析 HTTP Header，直接从当前线程绑定的 {@link SecurityContextHolder}
 *       中获取由 {@code JwtAuthenticationFilter} 预先装配好的用户认证主体（Principal）。</li>
 * </ol>
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 获取当前登录用户的详细个人信息
     * <p>
     * 从当前安全上下文中读取用户名，并返回角色、头像等结构化数据。
     * </p>
     *
     * @return 包含用户主体信息、角色权限与头像链接的统一响应 JSON
     */
    @GetMapping("/info")
    public Map<String, Object> getUserInfo() {
        // 1. 从当前线程绑定的 SecurityContext 中提取 Authentication 并获取用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 2. 封装用户画像数据（包含 DiceBear 动态矢量头像与预置管理员角色）
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
