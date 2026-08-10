package com.justdoit712.springboot.demo.logaop;

import com.justdoit712.springboot.demo.logaop.service.AopTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * AOP 通知测试类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 16:22
 */
@SpringBootTest
public class AopTest {

    @Autowired
    private AopTestService aopTestService;

    @Test
    public void testNormalExecution() {
        System.out.println("=============================================");
        System.out.println("====== 场景 1: 测试正常执行 ======");
        System.out.println("=============================================");
        aopTestService.doSomethingNormal("Hello AOP");
        System.out.println("=============================================\n");
    }

    @Test
    public void testExceptionExecution() {
        System.out.println("=============================================");
        System.out.println("====== 场景 2: 测试抛出异常 ======");
        System.out.println("=============================================");
        try {
            aopTestService.doSomethingError();
        } catch (Exception e) {
            System.out.println("====== 测试层捕获到异常: " + e.getMessage() + " ======");
        }
        System.out.println("=============================================\n");
    }
}
