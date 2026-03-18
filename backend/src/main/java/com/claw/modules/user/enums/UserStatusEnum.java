package com.claw.modules.user.enums;

import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatusEnum {
    DISABLED(0, "禁用"),
    NORMAL(1, "正常"),
    LOCKED(2, "锁定");

    private final int code;
    private final String description;

    UserStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static UserStatusEnum fromCode(int code) {
        for (UserStatusEnum status : UserStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return NORMAL;
    }

    /**
     * 根据描述获取枚举
     */
    public static UserStatusEnum fromDescription(String description) {
        for (UserStatusEnum status : UserStatusEnum.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        return NORMAL;
    }
}