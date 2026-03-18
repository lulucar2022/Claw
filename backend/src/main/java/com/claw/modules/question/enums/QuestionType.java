package com.claw.modules.question.enums;

import lombok.Getter;

/**
 * 题目类型枚举
 */
@Getter
public enum QuestionType {
    SINGLE_CHOICE("single", "单选题"),
    MULTIPLE_CHOICE("multiple", "多选题"),
    CODE("code", "代码题"),
    FILL_BLANK("fill", "填空题"),
    SHORT_ANSWER("short", "简答题");

    private final String code;
    private final String description;

    QuestionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static QuestionType fromCode(String code) {
        for (QuestionType type : QuestionType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return SINGLE_CHOICE;
    }

    /**
     * 根据描述获取枚举
     */
    public static QuestionType fromDescription(String description) {
        for (QuestionType type : QuestionType.values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        return SINGLE_CHOICE;
    }

    /**
     * 判断是否为选择题
     */
    public boolean isChoiceQuestion() {
        return this == SINGLE_CHOICE || this == MULTIPLE_CHOICE;
    }

    /**
     * 判断是否为代码题
     */
    public boolean isCodeQuestion() {
        return this == CODE;
    }

    /**
     * 判断是否为填空题
     */
    public boolean isFillQuestion() {
        return this == FILL_BLANK;
    }

    /**
     * 判断是否为简答题
     */
    public boolean isShortAnswerQuestion() {
        return this == SHORT_ANSWER;
    }
}