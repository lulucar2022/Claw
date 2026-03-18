package com.claw.common.exception;

/**
 * 数据不存在异常
 */
public class DataNotFoundException extends BusinessException {

    public DataNotFoundException(String message) {
        super(404, message);
    }

    public DataNotFoundException(String message, Object data) {
        super(404, message, data);
    }

    public static DataNotFoundException of(String message) {
        return new DataNotFoundException(message);
    }

    public static DataNotFoundException of(String message, Object data) {
        return new DataNotFoundException(message, data);
    }

    /**
     * 创建用户不存在异常
     */
    public static DataNotFoundException userNotFound(Long userId) {
        return new DataNotFoundException("用户不存在: " + userId);
    }

    public static DataNotFoundException userNotFound(String username) {
        return new DataNotFoundException("用户不存在: " + username);
    }

    /**
     * 创建题目不存在异常
     */
    public static DataNotFoundException questionNotFound(Long questionId) {
        return new DataNotFoundException("题目不存在: " + questionId);
    }

    /**
     * 创建分类不存在异常
     */
    public static DataNotFoundException categoryNotFound(Long categoryId) {
        return new DataNotFoundException("分类不存在: " + categoryId);
    }

    /**
     * 创建会话不存在异常
     */
    public static DataNotFoundException sessionNotFound(String sessionToken) {
        return new DataNotFoundException("会话不存在或已过期");
    }

    /**
     * 创建资源不存在异常
     */
    public static DataNotFoundException resourceNotFound(String resourceType, Long resourceId) {
        return new DataNotFoundException(resourceType + "不存在: " + resourceId);
    }
}