package com.claw.modules.question.enums;

import lombok.Getter;

/**
 * 难度级别枚举
 */
@Getter
public enum DifficultyLevel {
    EASY("easy", "简单"),
    MEDIUM("medium", "中等"),
    HARD("hard", "困难"),
    EXPERT("expert", "专家");

    private final String code;
    private final String description;

    DifficultyLevel(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static DifficultyLevel fromCode(String code) {
        for (DifficultyLevel level : DifficultyLevel.values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return MEDIUM;
    }

    /**
     * 根据描述获取枚举
     */
    public static DifficultyLevel fromDescription(String description) {
        for (DifficultyLevel level : DifficultyLevel.values()) {
            if (level.getDescription().equals(description)) {
                return level;
            }
        }
        return MEDIUM;
    }

    /**
     * 获取难度权重（用于排序和计算）
     */
    public int getWeight() {
        return switch (this) {
            case EASY -> 1;
            case MEDIUM -> 2;
            case HARD -> 3;
            case EXPERT -> 4;
        };
    }

    /**
     * 获取下一个难度级别
     */
    public DifficultyLevel getNext() {
        return switch (this) {
            case EASY -> MEDIUM;
            case MEDIUM -> HARD;
            case HARD -> EXPERT;
            case EXPERT -> EXPERT;
        };
    }

    /**
     * 获取上一个难度级别
     */
    public DifficultyLevel getPrevious() {
        return switch (this) {
            case EASY -> EASY;
            case MEDIUM -> EASY;
            case HARD -> MEDIUM;
            case EXPERT -> HARD;
        };
    }
}