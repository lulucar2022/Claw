package com.claw.common.exception;

/**
 * 数据已存在异常
 */
public class DataAlreadyExistsException extends BusinessException {

    public DataAlreadyExistsException(String message) {
        super(409, message);
    }

    public DataAlreadyExistsException(String message, Object data) {
        super(409, message, data);
    }

    public static DataAlreadyExistsException of(String message) {
        return new DataAlreadyExistsException(message);
    }

    public static DataAlreadyExistsException of(String message, Object data) {
        return new DataAlreadyExistsException(message, data);
    }

    /**
     * 创建用户已存在异常
     */
    public static DataAlreadyExistsException userAlreadyExists(String username) {
        return new DataAlreadyExistsException("用户已存在: " + username);
    }

    public static DataAlreadyExistsException userAlreadyExists(String username, String email) {
        return new DataAlreadyExistsException("用户已存在: " + username + " (" + email + ")");
    }

    /**
     * 创建题目已存在异常
     */
    public static DataAlreadyExistsException questionAlreadyExists(String title) {
        return new DataAlreadyExistsException("题目已存在: " + title);
    }

    /**
     * 创建分类已存在异常
     */
    public static DataAlreadyExistsException categoryAlreadyExists(String name) {
        return new DataAlreadyExistsException("分类已存在: " + name);
    }

    /**
     * 创建标签已存在异常
     */
    public static DataAlreadyExistsException tagAlreadyExists(String name) {
        return new DataAlreadyExistsException("标签已存在: " + name);
    }

    /**
     * 创建邮箱已存在异常
     */
    public static DataAlreadyExistsException emailAlreadyExists(String email) {
        return new DataAlreadyExistsException("邮箱已被注册: " + email);
    }

    /**
     * 创建手机号已存在异常
     */
    public static DataAlreadyExistsException phoneAlreadyExists(String phone) {
        return new DataAlreadyExistsException("手机号已被注册: " + phone);
    }

    /**
     * 创建资源已存在异常
     */
    public static DataAlreadyExistsException resourceAlreadyExists(String resourceType, String identifier) {
        return new DataAlreadyExistsException(resourceType + "已存在: " + identifier);
    }
}