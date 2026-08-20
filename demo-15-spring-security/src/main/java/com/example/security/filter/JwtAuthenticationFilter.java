package com.example.security.filter;

import com.example.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * <p>
 * JWT 认证拦截过滤器
 * </p>
 * <p>
 * <b>核心职责与设计要点：</b>
 * <ol>
 *   <li><b>单次请求保证：</b> 继承 {@link OncePerRequestFilter}，确保在任何 Servlet 容器或异步分发场景下，每个 HTTP 请求只被拦截过滤一次。</li>
 *   <li><b>凭证提取：</b> 拦截所有进入系统的 HTTP 请求，检查 Request Header 中是否带有 {@code Authorization: Bearer <token>}。</li>
 *   <li><b>验签与上下文装配：</b> 若携带有效 Token，调用 {@link JwtUtils} 进行签名与有效期校验；
 *       校验通过后，构造 Spring Security 认可的 {@link UsernamePasswordAuthenticationToken} 并存入 {@link SecurityContextHolder}。</li>
 *   <li><b>静默放行机制：</b> 若未携带 Token 或 Token 解析抛出异常（伪造/过期），本过滤器不会直接打断请求，
 *       而是保持上下文为空并继续放行，最终由过滤链末端的 {@code AuthorizationFilter} 统一切断并返回 401 Unauthorized。</li>
 * </ol>
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 17:15
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * 核心过滤逻辑：拦截请求并完成基于 JWT 的身份认证与安全上下文设置
     *
     * @param request     当前 HTTP 请求对象
     * @param response    当前 HTTP 响应对象
     * @param filterChain 过滤器链引用，用于将请求传递给下一个过滤器
     * @throws ServletException Servlet 异常
     * @throws IOException      I/O 异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从 HTTP 请求头中获取 Authorization 字段
        final String authHeader = request.getHeader("Authorization");

        // 2. 快速判断：若没有该 Header 或不是以 "Bearer " 规范开头，直接放行给下一个过滤器
        // （对于公开接口如 /api/auth/login，后续的 AuthorizationFilter 会予以放行；对于保护接口，后续会拦截为 401）
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 截取掉 "Bearer " 前缀（共 7 个字符），提取纯 JWT 字符串
        final String jwt = authHeader.substring(7);
        
        try {
            // 4. 从 Token 中提取用户名（Subject 声明）
            final String username = jwtUtils.extractUsername(jwt);

            // 5. 幂等性检查：当且仅当成功提取出用户名，且当前线程的安全上下文中尚未存在认证对象时才执行装配
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 6. 校验 Token 签名是否合法以及是否已过期
                if (jwtUtils.validateToken(jwt, username)) {
                    
                    // 7. 构造已认证的 Authentication 令牌对象
                    // 参数说明：principal（用户名/用户实体）、credentials（凭证，无状态下设为 null）、authorities（用户权限列表，此处为演示设为空集合）
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, 
                            null, 
                            Collections.emptyList()
                    );
                    
                    // 8. 补充请求相关的网络详细信息（例如客户端 IP、Session ID 等）
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 9. 将认证对象存入当前线程绑定的 SecurityContext 中
                    // 至此，当前请求被 Spring Security 标记为“已认证合法用户”
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // 10. 异常防御：若 Token 遭受篡改、算法不匹配、已过期等导致解析失败，捕获异常并不做处理
            // 此时 SecurityContextHolder 保持为空，后续的 AuthorizationFilter 会自动识别并返回 401
        }

        // 11. 将请求继续传递给过滤链中的下一个过滤器
        filterChain.doFilter(request, response);
    }
}
