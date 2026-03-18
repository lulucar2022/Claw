package com.claw.modules.question.enums;

import lombok.Getter;

/**
 * 题目状态枚举
 */
@Getter
public enum QuestionStatus {
    DRAFT(0, "草稿"),
    UNDER_REVIEW(1, "审核中"),
    PUBLISHED(2, "已发布"),
    UNPUBLISHED(3, "下架"),
    REJECTED(4, "审核拒绝");

    private final int code;
    private final String description;

    QuestionStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static QuestionStatus fromCode(int code) {
        for (QuestionStatus status : QuestionStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return DRAFT;
    }

    /**
     * 根据描述获取枚举
     */
    public static QuestionStatus fromDescription(String description) {
        for (QuestionStatus status : QuestionStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        return DRAFT;
    }

    /**
     * 判断状态是否允许查看（非登录用户）
     */
    public boolean isVisibleToPublic() {
        return this == PUBLISHED;
    }

    /**
     * 判断状态是否允许编辑
     */
    public boolean isEditable() {
        return this == DRAFT || this == REJECTED;
    }

    /**
     * 判断状态是否允许提交审核
     */
    public boolean canSubmitForReview() {
        return this == DRAFT || this == REJECTED;
    }

    /**
     * 判断状态是否允许发布
     */
    public boolean canPublish() {
        return this == UNDER_REVIEW;
    }

    /**
     * 判断状态是否允许下架
     */
    public boolean canUnpublish() {
        return this == PUBLISHED;
    }
}