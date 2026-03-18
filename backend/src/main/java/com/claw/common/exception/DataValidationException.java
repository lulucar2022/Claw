package com.claw.common.exception;

/**
 * 数据验证异常
 */
public class DataValidationException extends BusinessException {

    public DataValidationException(String message) {
        super(400, message);
    }

    public DataValidationException(String message, Object data) {
        super(400, message, data);
    }

    public static DataValidationException of(String message) {
        return new DataValidationException(message);
    }

    public static DataValidationException of(String message, Object data) {
        return new DataValidationException(message, data);
    }

    /**
     * 创建密码强度不足异常
     */
    public static DataValidationException passwordTooWeak() {
        return new DataValidationException("密码强度不足，至少8位，包含字母和数字");
    }

    /**
     * 创建邮箱格式错误异常
     */
    public static DataValidationException invalidEmailFormat(String email) {
        return new DataValidationException("邮箱格式错误: " + email);
    }

    /**
     * 创建手机号格式错误异常
     */
    public static DataValidationException invalidPhoneFormat(String phone) {
        return new DataValidationException("手机号格式错误: " + phone);
    }

    /**
     * 创建用户名格式错误异常
     */
    public static DataValidationException invalidUsernameFormat(String username) {
        return new DataValidationException("用户名格式错误，只能包含字母、数字和下划线，长度3-20位");
    }

    /**
     * 创建数据格式错误异常
     */
    public static DataValidationException invalidDataFormat(String field, String expectedFormat) {
        return new DataValidationException(field + "格式错误，应为: " + expectedFormat);
    }

    /**
     * 创建数据长度异常
     */
    public static DataValidationException invalidDataLength(String field, int min, int max) {
        return new DataValidationException(field + "长度应在" + min + "到" + max + "之间");
    }

    /**
     * 创建数据范围异常
     */
    public static DataValidationException invalidDataRange(String field, Number min, Number max) {
        return new DataValidationException(field + "应在" + min + "到" + max + "之间");
    }

    /**
     * 创建必填字段缺失异常
     */
    public static DataValidationException requiredFieldMissing(String field) {
        return new DataValidationException("必填字段缺失: " + field);
    }

    /**
     * 创建数据关联异常
     */
    public static DataValidationException invalidDataRelation(String relation) {
        return new DataValidationException("数据关联错误: " + relation);
    }

    /**
     * 创建业务规则违反异常
     */
    public static DataValidationException businessRuleViolation(String rule) {
        return new DataValidationException("违反业务规则: " + rule);
    }

    /**
     * 创建状态转换异常
     */
    public static DataValidationException invalidStateTransition(String fromState, String toState) {
        return new DataValidationException("状态转换无效: " + fromState + " -> " + toState);
    }

    /**
     * 创建重复操作异常
     */
    public static DataValidationException duplicateOperation(String operation) {
        return new DataValidationException("重复操作: " + operation);
    }

    /**
     * 创建过期数据异常
     */
    public static DataValidationException expiredData(String dataType) {
        return new DataValidationException(dataType + "已过期");
    }

    /**
     * 创建无效参数异常
     */
    public static DataValidationException invalidParameter(String parameter, String reason) {
        return new DataValidationException("参数无效: " + parameter + " - " + reason);
    }
}