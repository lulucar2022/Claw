package com.claw.modules.user.enums;

import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
public enum GenderEnum {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final int code;
    private final String description;

    GenderEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static GenderEnum fromCode(int code) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getCode() == code) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    /**
     * 根据描述获取枚举
     */
    public static GenderEnum fromDescription(String description) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getDescription().equals(description)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
}