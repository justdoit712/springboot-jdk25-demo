package com.justdoit712.springboot.demo.logaop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p>
 * AOP 通知测试服务
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-08-10 16:22
 */
@Service
public class AopTestService {
    private static final Logger log = LoggerFactory.getLogger(AopTestService.class);

    public String doSomethingNormal(String input) {
        log.info("--- [Target Method] doSomethingNormal is executing, input: {} ---", input);
        return "Success: " + input;
    }

    public void doSomethingError() {
        log.info("--- [Target Method] doSomethingError is executing ---");
        throw new RuntimeException("Simulated exception from target method");
    }
}
