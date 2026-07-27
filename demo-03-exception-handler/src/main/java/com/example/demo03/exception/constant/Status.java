package com.example.demo03.exception.constant;

/**
 * <p>
 * 状态码枚举
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
public enum Status {
    /**
     * 操作成功
     */
    OK(200, "操作成功"),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(500, "服务器未知异常"),
    
    /**
     * 业务错误
     */
    BUSINESS_ERROR(400, "业务逻辑异常");

    private final Integer code;
    private final String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
