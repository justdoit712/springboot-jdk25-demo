package com.example.demo03.exception.exception;

import com.example.demo03.exception.constant.Status;

/**
 * <p>
 * 自定义业务异常基础类
 * </p>
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
public class BaseException extends RuntimeException {
    
    private final Integer code;

    public BaseException(Status status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
