package com.justdoit712.springboot.demo.logaop.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * 统一日志切面
 * 像一个“隐形保镖”拦截所有 Controller 的请求
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 15:47
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义切点：拦截 controller 包下的所有类的所有方法
     */
    @Pointcut("execution(public * com.justdoit712.springboot.demo.logaop.controller..*.*(..))")
    public void logPointCut() {
    }

    /**
     * 环绕通知：在方法执行前后进行拦截
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 1. 获取 Request 对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 2. 打印请求信息
        if (request != null) {
            log.info("========================================== Start ==========================================");
            log.info("URL            : {}", request.getRequestURL().toString());
            log.info("HTTP Method    : {}", request.getMethod());
            log.info("Class Method   : {}.{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
            log.info("IP             : {}", getClientIp(request));
            log.info("Request Args   : {}", JSONUtil.toJsonStr(point.getArgs()));
        }

        // 3. 执行真正的业务方法
        Object result;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error("Exception      : {}", e.getMessage());
            log.info("=========================================== End ===========================================");
            log.info("");
            throw e; // 继续向上抛出异常，让全局异常处理器接管
        }

        // 4. 打印响应信息和耗时
        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Response Args  : {}", JSONUtil.toJsonStr(result));
        log.info("Time Taken     : {} ms", timeTaken);
        log.info("=========================================== End ===========================================");
        log.info("");

        return result;
    }

    /**
     * 获取客户端真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个 ip 值，第一个才是真实 ip
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}
