package com.claw.common.exception;

import lombok.Getter;

/**
 * 业务异常基类
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误数据
     */
    private final Object data;

    /**
     * 构造方法
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.data = null;
    }

    /**
     * 构造方法（带数据）
     */
    public BusinessException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    /**
     * 构造方法（使用默认错误码）
     */
    public BusinessException(String message) {
        this(500, message);
    }

    /**
     * 构造方法（使用默认错误码和Throwable）
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.data = null;
    }

    /**
     * 构造方法（使用Throwable）
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = null;
    }

    /**
     * 快速创建业务异常
     */
    public static BusinessException of(Integer code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 快速创建业务异常（带数据）
     */
    public static BusinessException of(Integer code, String message, Object data) {
        return new BusinessException(code, message, data);
    }

    /**
     * 快速创建业务异常（使用默认错误码）
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 创建参数错误异常
     */
    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }

    /**
     * 创建未授权异常
     */
    public static BusinessException unauthorized(String message) {
        return new BusinessException(401, message);
    }

    /**
     * 创建禁止访问异常
     */
    public static BusinessException forbidden(String message) {
        return new BusinessException(403, message);
    }

    /**
     * 创建资源不存在异常
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    /**
     * 创建请求过于频繁异常
     */
    public static BusinessException tooManyRequests(String message) {
        return new BusinessException(429, message);
    }

    /**
     * 创建服务器内部错误异常
     */
    public static BusinessException internalServerError(String message) {
        return new BusinessException(500, message);
    }

    /**
     * 创建服务不可用异常
     */
    public static BusinessException serviceUnavailable(String message) {
        return new BusinessException(503, message);
    }
}