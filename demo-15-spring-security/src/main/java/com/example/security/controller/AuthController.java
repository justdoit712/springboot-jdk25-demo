package com.example.security.controller;

import com.example.security.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 认证与授权控制器（发牌接口）
 * </p>
 * <p>
 * <b>核心职责与设计要点：</b>
 * <ol>
 *   <li><b>公开白名单接口：</b> 该控制器下的所有路由（{@code /api/auth/**}）已在 {@code SecurityConfig} 中配置为 {@code permitAll()}，
 *       客户端无需携带任何 Token 即可发起请求。</li>
 *   <li><b>凭证校验与发牌：</b> 接收客户端提交的账号密码，校验通过后调用 {@link JwtUtils#generateToken(String)}
 *       签发具备 HMAC-SHA256 数字签名的 JWT 令牌，并返回给客户端持久化存储（如 localStorage）。</li>
 *   <li><b>无状态响应：</b> 登录成功后服务端不创建任何 Session，仅返回包含了身份声明的 Token 密文。</li>
 * </ol>
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * JWT 工具类，用于签发与生成 Token
     */
    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * 用户登录接口
     * <p>
     * 接收登录表单 JSON 数据，校验用户名与密码。若校验成功则签发有效时长为 24 小时的 JWT Token。
     * </p>
     *
     * @param request 包含用户名与密码的登录请求数据传输对象 {@link LoginRequest}
     * @return 登录成功返回 200 OK 及 Token 载荷；校验失败返回 401 Unauthorized 及错误信息
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. 模拟账密比对逻辑（实际生产环境中通常调用 AuthenticationManager 或 UserDetailsService 查询数据库并进行 BCrypt 解密比对）
        if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
            
            // 2. 账密正确：使用 JJWT 工具类根据当前用户名签发 JWT 令牌
            String token = jwtUtils.generateToken(request.getUsername());
            
            // 3. 封装统一响应体并返回 200 状态码
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "Login successful",
                    "data", Map.of("token", token)
            ));
        }
        
        // 4. 账密不匹配：返回 HTTP 401 未授权状态码与提示信息
        return ResponseEntity.status(401).body(Map.of(
                "code", 401,
                "message", "Invalid username or password"
        ));
    }

    /**
     * <p>
     * 登录请求参数 DTO (Data Transfer Object)
     * </p>
     * 用于接收并反序列化客户端 POST 提交的 JSON 格式登录凭证。
     */
    public static class LoginRequest {
        
        /**
         * 登录用户名
         */
        private String username;
        
        /**
         * 登录密码（明文传输，生产环境建议配合 HTTPS 传输）
         */
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
