package com.claw.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一API响应格式
 * 根据API文档中的响应格式设计
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 设置响应数据（链式调用）
     */
    public ApiResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;

    /**
     * 请求ID（用于追踪）
     */
    private String requestId;

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return success(null, "Success");
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Success");
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    /**
     * 失败响应（使用默认错误码）
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }

    /**
     * 参数错误响应
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 未授权响应
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 资源不存在响应
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 请求过于频繁响应
     */
    public static <T> ApiResponse<T> tooManyRequests(String message) {
        return error(429, message);
    }

    /**
     * 服务器内部错误响应
     */
    public static <T> ApiResponse<T> internalServerError(String message) {
        return error(500, message);
    }

    /**
     * 设置请求ID
     */
    public ApiResponse<T> withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    /**
     * 判断响应是否成功
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }

    /**
     * 判断响应是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 获取HTTP状态码
     */
    public int getHttpStatus() {
        if (code == null) {
            return 500;
        }
        return code;
    }
}