package com.example.demo03.exception.controller;

import com.example.demo03.exception.constant.Status;
import com.example.demo03.exception.exception.BaseException;
import com.example.demo03.exception.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 异常测试接口
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/success")
    public ApiResponse<String> success() {
        return ApiResponse.ofSuccess("请求成功获取到了数据！");
    }

    @GetMapping("/business-error")
    public ApiResponse<String> businessError() {
        // 模拟抛出自定义的业务异常，GlobalExceptionHandler 会拦截并包装为 ApiResponse 返回
        throw new BaseException(Status.BUSINESS_ERROR);
    }

    @GetMapping("/system-error")
    public ApiResponse<String> systemError() {
        // 模拟发生算术异常 (除零)，会被 Exception.class 处理
        int result = 1 / 0;
        return ApiResponse.ofSuccess("这行代码永远不会执行到" + result);
    }
}
