package com.justdoit712.springboot.demo.logaop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 演示所有类型 AOP 通知的切面。
 * 包含：@Before, @Around, @AfterReturning, @AfterThrowing, @After
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 16:22
 */
@Aspect
@Component
public class AllAdvicesAspect {
    private static final Logger log = LoggerFactory.getLogger(AllAdvicesAspect.class);

    /**
     * 定义切点 (Pointcut)
     * 拦截 com.justdoit712.springboot.demo.logaop.service.AopTestService 下的所有公共方法
     */
    @Pointcut("execution(public * com.justdoit712.springboot.demo.logaop.service.AopTestService.*(..))")
    public void testPointCut() {
    }

    /**
     * 前置通知 (@Before)
     * 在目标方法执行之前触发，不能阻止目标方法执行。
     * 
     * @param joinPoint 连接点，包含了目标方法的相关信息
     */
    @Before("testPointCut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info("[@Before 前置通知] 准备执行: {}", joinPoint.getSignature().getName());
    }

    /**
     * 环绕通知 (@Around)
     * 最强大的通知，包裹整个目标方法的执行。可以在执行前后自定义逻辑，
     * 并且决定是否真正调用 proceed() 来执行目标方法。
     * 
     * @param joinPoint 必须使用 ProceedingJoinPoint，以支持 proceed() 的调用
     * @return 目标方法的返回值
     * @throws Throwable 目标方法执行抛出的异常
     */
    @Around("testPointCut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[@Around 环绕通知 - 执行前] 开始环绕: {}", joinPoint.getSignature().getName());
        Object result;
        try {
            result = joinPoint.proceed(); // 显式调用 proceed() 执行真实业务目标方法
            log.info("[@Around 环绕通知 - 执行后] 目标方法正常返回");
        } catch (Throwable e) {
            log.error("[@Around 环绕通知 - 异常] 目标方法抛出异常");
            throw e; // 捕获后可以做处理，但通常需要继续向上抛出，否则异常会被吞掉
        }
        return result;
    }

    /**
     * 返回通知 (@AfterReturning)
     * 只有当目标方法正常执行完成（没有抛出异常）才会触发。
     * 
     * @param joinPoint 连接点
     * @param result 目标方法的实际返回值，名字需与注解中的 returning 属性保持一致
     */
    @AfterReturning(pointcut = "testPointCut()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        log.info("[@AfterReturning 返回通知] 方法 {} 成功返回, 结果: {}", joinPoint.getSignature().getName(), result);
    }

    /**
     * 异常通知 (@AfterThrowing)
     * 只有当目标方法执行期间抛出异常时才会触发。
     * 
     * @param joinPoint 连接点
     * @param ex 目标方法抛出的实际异常，名字需与注解中的 throwing 属性保持一致
     */
    @AfterThrowing(pointcut = "testPointCut()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        log.error("[@AfterThrowing 异常通知] 方法 {} 抛出异常: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }

    /**
     * 后置/最终通知 (@After)
     * 无论目标方法是正常执行结束还是抛出异常，都会最后触发，类似于 try-catch 中的 finally 块。
     * 
     * @param joinPoint 连接点
     */
    @After("testPointCut()")
    public void afterAdvice(JoinPoint joinPoint) {
        log.info("[@After 后置/最终通知] 方法 {} 结束了 (无论成功失败都会走这里)", joinPoint.getSignature().getName());
    }
}
