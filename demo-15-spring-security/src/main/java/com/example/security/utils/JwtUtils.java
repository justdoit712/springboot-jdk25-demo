package com.example.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * <p>
 * JWT (JSON Web Token) 工具类
 * </p>
 * <p>
 * <b>技术要点与版本演进：</b>
 * <ol>
 *   <li><b>JJWT 0.12.x 现代 API：</b> 本工具类全面采用了 JJWT 0.12+ 的最新规范，
 *       弃用了过时的 {@code parserBuilder()} / {@code parseClaimsJws()}，
 *       升级为 {@code Jwts.parser().verifyWith(...).build().parseSignedClaims(...).getPayload()}。</li>
 *   <li><b>HMAC-SHA256 安全签名：</b> 基于配置的密钥生成符合密码学标准的 {@link SecretKey}，确保令牌防篡改。</li>
 *   <li><b>函数式 Claim 解析：</b> 提供高阶函数 {@link #extractClaim(String, Function)}，方便提取自定义或标准的 JWT 负载声明。</li>
 * </ol>
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@Component
public class JwtUtils {

    /**
     * JWT 签名密钥（由 application.yml 注入，需满足至少 256 位长度以支持 HS256）
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT 令牌有效时长（单位：毫秒，由 application.yml 注入）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据明文密钥生成 HMAC-SHA 安全签名 Key
     *
     * @return 符合 HS256 算法要求的 SecretKey 密钥对象
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 为指定用户签发 JWT 令牌
     *
     * @param username 用户名（作为 JWT 的 Subject 主题）
     * @return 紧凑格式（Compact）的三段式 JWT 字符串 (Header.Payload.Signature)
     */
    public String generateToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                // 设置主题（通常为用户唯一标识/用户名）
                .subject(username)
                // 签发时间（iat）
                .issuedAt(new Date(currentTimeMillis))
                // 过期时间（exp）
                .expiration(new Date(currentTimeMillis + expiration))
                // 采用指定密钥进行 HMAC-SHA256 签名
                .signWith(getSigningKey())
                // 压缩生成最终的 JWT 字符串
                .compact();
    }

    /**
     * 从 JWT 令牌中提取用户名 (Subject)
     *
     * @param token JWT 字符串
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 JWT 令牌中提取过期时间 (Expiration)
     *
     * @param token JWT 字符串
     * @return 过期时间 Date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 通用函数式方法：解析并提取 JWT Claims 中的指定属性
     *
     * @param token          JWT 字符串
     * @param claimsResolver 函数式声明提取器（如 Claims::getSubject）
     * @param <T>            目标属性类型
     * @return 提取出的属性值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 底层解析：验证签名并提取 Token 中的所有 Claims 声明
     * <p>
     * <b>注意：</b> 若 Token 被篡改、算法不匹配或已过期，本方法将直接抛出异常（如 {@code JwtException}）。
     * </p>
     *
     * @param token JWT 字符串
     * @return Claims 负载对象
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                // 设置验签密钥
                .verifyWith(getSigningKey())
                .build()
                // 解析已签名的 JWT（若验签失败将在此抛出异常）
                .parseSignedClaims(token)
                // 获取 Payload 内容
                .getPayload();
    }

    /**
     * 检查 JWT 令牌是否已过期
     *
     * @param token JWT 字符串
     * @return true 表示已过期，false 表示尚未过期
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 全面校验 JWT 令牌的合法性
     *
     * @param token    JWT 字符串
     * @param username 预期的用户名
     * @return true 表示令牌合法有效，false 表示用户名不匹配或令牌已过期
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        // 校验条件：提取的用户名与当前用户匹配，且未过有效期
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
