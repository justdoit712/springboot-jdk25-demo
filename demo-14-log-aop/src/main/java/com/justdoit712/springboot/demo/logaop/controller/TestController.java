package com.justdoit712.springboot.demo.logaop.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 测试接口
 * 注意：我们在方法内部故意不写任何 log.info()，以此验证 AOP 是否生效
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-31 15:47
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * 测试普通的 GET 请求
     *
     * @param name 测试参数
     * @return 响应数据
     */
    @GetMapping("/hello")
    public Map<String, Object> hello(@RequestParam(defaultValue = "World") String name) {
        // 模拟一点耗时操作
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Hello, " + name + "!");
        return result;
    }

    /**
     * 测试发生异常时的请求
     *
     * @return 永远抛出异常
     */
    @GetMapping("/error")
    public String testError() {
        // 模拟业务逻辑抛出异常
        int a = 1 / 0;
        return "如果你看到这个，说明出鬼了";
    }
}
