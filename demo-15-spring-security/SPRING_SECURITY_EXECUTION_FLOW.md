# Spring Security 6 真实执行顺序与过滤链生命周期

本文档详细记录了 `demo-15-spring-security` 模块中，从客户端 HTTP 请求到达服务端，到穿透 Spring Security 各级过滤器，最终进入业务 Controller 的完整真实执行链路。

---

## 一、 核心设计哲学：Authentication 先于 Authorization

Spring Security 的底层架构严格遵循分层决策模型：

$$\text{HTTP Request} \longrightarrow \underbrace{\text{Authentication（身份识别 / 你是谁）}}_{\text{JwtAuthenticationFilter}} \longrightarrow \underbrace{\text{Authorization（权限判定 / 你能否访问）}}_{\text{AuthorizationFilter (permitAll / authenticated)}} \longrightarrow \text{Controller}$$

1. **认证阶段（Authentication）必须在前**：系统必须先尝试解析请求携带的凭证（如 Header 中的 Bearer JWT），明确当前请求是**已知合法用户**还是**匿名用户**。
2. **鉴权阶段（Authorization）必须在后**：在明确请求身份后，由位于过滤链末端的决策器根据路径匹配规则（`permitAll()` 或 `authenticated()`）决定放行还是抛出拒绝访问异常。

---

## 二、 完整调用栈与过滤器真实执行链路

当一个 HTTP 请求打到 Spring Boot 服务端（端口 8080）时，其宏观与微观层面的完整执行顺序如下：

```text
客户端发送 HTTP 请求 (例如 POST /api/auth/login 或 GET /api/user/info)
   │
   ▼
1. Servlet 容器层 (Tomcat)
   │  StandardEngine -> StandardHost -> StandardContext -> ApplicationFilterChain
   │
   ▼
2. Spring 桥接层 (DelegatingFilterProxy)
   │  将原生 Servlet 请求转发给 Spring IoC 容器中管理的 FilterChainProxy
   │
   ▼
3. Spring Security 总入口 (FilterChainProxy)
   │  定位并执行匹配的 SecurityFilterChain（由 SecurityConfig.java 组装）
   │
   ▼
【Spring Security 内部过滤器链 (SecurityFilterChain) 真实执行顺序】
   │
   ├─► ① DisableEncodeUrlFilter / WebAsyncManagerIntegrationFilter
   │      - 内部基础适配与异步请求支持
   │
   ├─► ② SecurityContextHolderFilter
   │      - 初始化并清理当前线程的 SecurityContext，避免线程复用导致上下文污染
   │
   ├─► ③ HeaderWriterFilter
   │      - 向 Response 中写入安全相关的标准响应头（X-Content-Type-Options、HSTS 等）
   │
   ├─► ④ CorsFilter（最先处理跨域）
   │      - 根据 SecurityConfig 中的 corsConfigurationSource() 处理跨域 OPTIONS 预检请求及 CORS 响应头
   │
   ├─► ⑤ JwtAuthenticationFilter（【我们自定义的过滤器】最先尝试认人）
   │      │
   │      ├─ 解析 Authorization 请求头是否包含 "Bearer <token>"
   │      ├─ [有有效 Token] -> JwtUtils 验签通过 -> 将 UsernamePasswordAuthenticationToken 存入 SecurityContextHolder
   │      └─ [无 Token / 签名无效] -> 不报错，直接调用 filterChain.doFilter() 静默放行给后续过滤器
   │
   ├─► ⑥ RequestCacheAwareFilter / SecurityContextHolderAwareRequestFilter
   │      - 包装原始 HttpServletRequest，支持 HttpServletRequest.getUserPrincipal() 等标准 API
   │
   ├─► ⑦ AnonymousAuthenticationFilter（匿名身份兜底）
   │      - 检查 SecurityContextHolder，若当前仍无认证信息，则为该请求赋予匿名身份（ROLE_ANONYMOUS）
   │
   ├─► ⑧ SessionManagementFilter
   │      - 遵循 SessionCreationPolicy.STATELESS 策略，不创建、不持久化 HttpSession
   │
   ├─► ⑨ ExceptionTranslationFilter（安全异常翻译器）
   │      - 捕获下游抛出的 AccessDeniedException 或 AuthenticationException，转换为 401/403 响应
   │
   └─► ⑩ AuthorizationFilter（【权限最终决策器，位于过滤链最末端】）
          │
          ├─ 匹配 /api/auth/** -> 属于 permitAll() -> 无论是否登录，无条件放行！
          └─ 匹配其他路径 (/api/user/**) -> 属于 authenticated() -> 检查是否已认证：
                ├─ 已认证 (有 Token 用户) -> 允许通过过滤链
                └─ 未认证 (匿名用户) -> 抛出 AccessDeniedException -> 由上一层 ExceptionTranslationFilter 拦截并返回 401
   │
   ▼
4. Spring MVC 调度层 (DispatcherServlet)
   │  匹配 HandlerMapping -> 进入 HandlerInterceptor 链
   │
   ▼
5. 业务控制器 (Controller)
   │  - AuthController.login() 处理登录逻辑
   │  - UserController.getUserInfo() 从 SecurityContextHolder 读取用户名并返回数据
```

---

## 三、 三大核心场景的执行流程对比

### 场景 1：第一次登录 (`POST /api/auth/login`，无 Token)

| 步骤 | 处理组件 | 动作与判定 | 结果 |
| :--- | :--- | :--- | :--- |
| 1 | `CorsFilter` | 校验 Origin 并放行 POST 请求 | 通过 |
| 2 | **`JwtAuthenticationFilter`** | 检查 Header，发现无 `Authorization` 头，**不做任何拦截**，直接 `filterChain.doFilter()` | 通过（上下文仍为空） |
| 3 | `AnonymousAuthenticationFilter` | 发现上下文为空，为请求打上匿名标识 | 标记为匿名用户 |
| 4 | **`AuthorizationFilter`** | 校验 URL `/api/auth/login`，匹配到 `.requestMatchers("/api/auth/**").permitAll()` 规则 | **放行通过过滤链** |
| 5 | `DispatcherServlet` -> `AuthController` | 校验账密 `admin / 123456`，调用 `JwtUtils.generateToken("admin")` 签发 JWT | 返回 200 OK + Token |

---

### 场景 2：携带合法 Token 访问保护接口 (`GET /api/user/info`)

| 步骤 | 处理组件 | 动作与判定 | 结果 |
| :--- | :--- | :--- | :--- |
| 1 | `CorsFilter` | 校验跨域并放行 | 通过 |
| 2 | **`JwtAuthenticationFilter`** | 提取 `Bearer <jwt>`，`JwtUtils.validateToken` 验签成功，将 `admin` 写入 `SecurityContextHolder` | **成功装配已认证身份** |
| 3 | `AnonymousAuthenticationFilter` | 检查发现上下文中已有认证信息，跳过 | 跳过 |
| 4 | **`AuthorizationFilter`** | 校验 URL `/api/user/info`，规则为 `anyRequest().authenticated()`；检查上下文为已认证状态 | **放行通过过滤链** |
| 5 | `DispatcherServlet` -> `UserController` | 执行 `SecurityContextHolder.getContext().getAuthentication().getName()` 获取用户身份 | 返回 200 OK + 用户数据 |

---

### 场景 3：携带伪造 / 过期 Token 访问保护接口 (`GET /api/user/info`)

| 步骤 | 处理组件 | 动作与判定 | 结果 |
| :--- | :--- | :--- | :--- |
| 1 | `CorsFilter` | 校验跨域并放行 | 通过 |
| 2 | **`JwtAuthenticationFilter`** | 提取 Token 验签抛出异常（签名错误或过期），捕获异常，**不向上下文注入任何信息**，直接放行 | 上下文保持为空 |
| 3 | `AnonymousAuthenticationFilter` | 上下文为空，标记为匿名用户 | 标记为匿名用户 |
| 4 | **`AuthorizationFilter`** | 校验 URL `/api/user/info`（需要 `authenticated()`），发现当前是匿名用户，**拒绝访问，抛出未认证异常** | 触发安全异常 |
| 5 | `ExceptionTranslationFilter` | 捕获到未认证异常，终止流程，直接向客户端返回 **401 Unauthorized** 响应 | 返回 401，前端跳回登录页 |

---

## 四、 关键后端源码位置索引

- **过滤链装配与放行规则**：[`SecurityConfig.java`](./src/main/java/com/example/security/config/SecurityConfig.java)
- **JWT 提取与上下文注入过滤器**：[`JwtAuthenticationFilter.java`](./src/main/java/com/example/security/filter/JwtAuthenticationFilter.java)
- **JJWT 0.12.x 签名与验签工具**：[`JwtUtils.java`](./src/main/java/com/example/security/utils/JwtUtils.java)
- **公开认证控制器（发牌）**：[`AuthController.java`](./src/main/java/com/example/security/controller/AuthController.java)
- **受保护业务控制器（消费凭证）**：[`UserController.java`](./src/main/java/com/example/security/controller/UserController.java)
