package com.example.demo03.exception.model;

import com.example.demo03.exception.constant.Status;

/**
 * <p>
 * 统一 API 响应结果封装
 * </p >
 *
 * @author justdoit712
 * @date Created in 2026-07-27 13:42
 */
public class ApiResponse<T> {
    /** 响应状态码 */
    private Integer code;
    /** 响应提示消息 */
    private String message;
    /** 响应业务数据 */
    private T data;

    /** 私有化无参构造函数，禁止外部直接 new */
    private ApiResponse() {
    }

    /** 私有化全参构造函数，强制通过静态工厂方法创建对象 */
    private ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 根据预定义的状态码枚举构建响应（不带业务数据 data = null）
     *
     * @param status 状态码枚举（如 Status.BUSINESS_ERROR）
     * @param <T>    响应数据的泛型类型
     * @return ApiResponse 响应封装对象
     */
    public static <T> ApiResponse<T> ofStatus(Status status) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), null);
    }

    /**
     * 根据预定义的状态码枚举和业务数据构建响应
     *
     * @param status 状态码枚举
     * @param data   要返回给前端的具体业务数据
     * @param <T>    响应数据的泛型类型
     * @return ApiResponse 响应封装对象
     */
    public static <T> ApiResponse<T> ofStatus(Status status, T data) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), data);
    }

    /**
     * 自定义状态码、提示信息和业务数据的底层构建方法
     *
     * @param code    HTTP 或自定义业务状态码
     * @param message 提示信息（支持动态拼接异常文本）
     * @param data    响应业务数据
     * @param <T>    响应数据的泛型类型
     * @return ApiResponse 响应封装对象
     */
    public static <T> ApiResponse<T> of(Integer code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    /**
     * 快速构建【操作成功】且【带有数据】的响应对象（状态码 200，消息 "操作成功"）
     *
     * @param data 返回给前端的业务数据
     * @param <T>  响应数据的泛型类型
     * @return ApiResponse 响应封装对象
     */
    public static <T> ApiResponse<T> ofSuccess(T data) {
        return ofStatus(Status.OK, data);
    }

    /**
     * 快速构建【操作成功】且【仅提示文本】的响应对象（如“修改成功”、“删除成功”）
     *
     * @param message 自定义成功提示文本
     * @param <T>     响应数据的泛型类型
     * @return ApiResponse 响应封装对象
     */
    public static <T> ApiResponse<T> ofMessage(String message) {
        return of(Status.OK.getCode(), message, null);
    }

    /**
     * 获取状态码（供 Jackson 序列化 JSON 及外部读取）
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取提示消息（供 Jackson 序列化 JSON 及外部读取）
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取业务数据（供 Jackson 序列化 JSON 及外部读取）
     */
    public T getData() {
        return data;
    }
}
