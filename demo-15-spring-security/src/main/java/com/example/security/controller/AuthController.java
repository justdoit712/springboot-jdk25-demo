package com.example.security.controller;

import com.example.security.utils.JwtUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * Auth Controller for login and token generation
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Mock authentication check
        if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
            String token = jwtUtils.generateToken(request.getUsername());
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "Login successful",
                    "data", Map.of("token", token)
            ));
        }
        return ResponseEntity.status(401).body(Map.of(
                "code", 401,
                "message", "Invalid username or password"
        ));
    }

    public static class LoginRequest {
        private String username;
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
