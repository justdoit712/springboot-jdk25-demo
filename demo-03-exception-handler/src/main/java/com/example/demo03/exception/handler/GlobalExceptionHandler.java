package com.example.demo03.exception.handler;

import com.example.demo03.exception.exception.BaseException;
import com.example.demo03.exception.model.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局统一异常处理类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BaseException.class)
    public ApiResponse<Object> handleBaseException(BaseException e) {
        return ApiResponse.of(e.getCode(), e.getMessage(), null);
    }

    /**
     * 处理除自定义异常外的所有未知异常，例如空指针等
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception e) {
        return ApiResponse.of(500, "系统内部发生错误：" + e.getMessage(), null);
    }
}
