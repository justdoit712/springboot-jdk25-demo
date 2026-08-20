package com.example.security.config;

import com.example.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * <p>
 * Spring Security 6 核心安全配置类
 * </p>
 * <p>
 * <b>核心设计理念：</b>
 * <ol>
 *   <li><b>组件化配置（Bean 模式）：</b> Spring Security 6 彻底弃用了旧版的 {@code WebSecurityConfigurerAdapter} 继承模式，
 *       全面转向基于 {@link SecurityFilterChain} 的 Bean 注入与 Lambda DSL 配置风格。</li>
 *   <li><b>纯无状态架构（Stateless）：</b> 关闭服务器端 Session 机制，每一次请求都必须携带自包含的 JWT 凭证，
 *       由挂载的自定义过滤器 {@link JwtAuthenticationFilter} 进行拦截验签并动态装配安全上下文。</li>
 *   <li><b>安全职责分离：</b> 明确划分公开接口（如登录接口 {@code /api/auth/**}）与受保护业务接口，
 *       并在过滤链的最前沿处理 CORS 跨域请求与 CSRF 防护策略。</li>
 * </ol>
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 自定义 JWT 认证过滤器，用于拦截 HTTP 请求并解析验证 Authorization 请求头中的 Bearer Token
     */
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * 配置 Spring Security 过滤链 (SecurityFilterChain)
     * <p>
     * 本方法定义了整个系统的安全拦截策略，包括跨域处理、CSRF 禁用、接口放行规则、会话管理策略以及自定义过滤器的插入位置。
     * </p>
     *
     * @param http Spring Security 提供的 HttpSecurity 安全构建器
     * @return 构建好的 SecurityFilterChain 过滤器链实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 配置 CORS 跨域处理
            // 绑定自定义的 CorsConfigurationSource，确保前后端分离架构下的跨域预检（OPTIONS）和常规请求能正常通过
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 2. 禁用 CSRF (跨站请求伪造)
            // 原理：在基于 JWT 的无状态 RESTful API 架构中，客户端通过 HTTP Request Header (Authorization) 传递凭证，
            // 浏览器不会像传统 Cookie 认证那样自动附带身份凭证，天生免疫 CSRF 攻击，因此可以安全地禁用。
            .csrf(AbstractHttpConfigurer::disable)

            // 3. 配置 URL 访问授权规则
            .authorizeHttpRequests(auth -> auth
                // 认证相关接口（如登录 /api/auth/login）无需任何身份凭证，无条件对外公开放行
                .requestMatchers("/api/auth/**").permitAll()
                // 除上述放行的白名单路径外，其余所有请求必须经过认证（携带有效 Token）方可访问
                .anyRequest().authenticated()
            )

            // 4. 配置会话管理策略 (Session Management)
            // 设置为 STATELESS (无状态)：Spring Security 将绝不创建或使用 HttpSession 来存储用户的 SecurityContext
            // 每次请求的状态完全由客户端持有的 JWT 维持，极大地降低了服务端内存占用并天然支持横向分布式扩展
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 5. 挂载自定义 JWT 过滤器
            // 将 JwtAuthenticationFilter 插入到内置的 UsernamePasswordAuthenticationFilter 之前
            // 确保在执行用户名密码表单校验之前，就已经优先尝试从请求头中解析出 JWT 并装配好 SecurityContextHolder
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 配置 CORS (跨源资源共享) 规则
     * <p>
     * 允许来自前端单页面应用 (Vue 3 / Vite SPA 等) 的跨域 HTTP 请求。
     * </p>
     *
     * @return 基于 URL 匹配的 CorsConfigurationSource 跨域配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许所有来源模式（生产环境中建议替换为明确的前端域名，如 http://localhost:5173）
        configuration.setAllowedOriginPatterns(List.of("*"));
        
        // 允许常见的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 允许客户端请求携带任何自定义 Header（例如 Authorization、Content-Type 等）
        configuration.setAllowedHeaders(List.of("*"));
        
        // 允许请求携带用户凭据（如 Credentials / Cookies）
        configuration.setAllowCredentials(true);
        
        // 将上述跨域规则注册并应用到所有接口路径 ("/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
