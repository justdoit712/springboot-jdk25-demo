package com.example.demo03.exception.model;

import com.example.demo03.exception.constant.Status;

/**
 * <p>
 * 统一 API 响应结果封装
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    private ApiResponse() {
    }

    private ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ofStatus(Status status) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), null);
    }

    public static <T> ApiResponse<T> ofStatus(Status status, T data) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), data);
    }

    public static <T> ApiResponse<T> of(Integer code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> ofSuccess(T data) {
        return ofStatus(Status.OK, data);
    }

    public static <T> ApiResponse<T> ofMessage(String message) {
        return of(Status.OK.getCode(), message, null);
    }
    
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
