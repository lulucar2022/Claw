package com.claw.modules.user.entity;

import com.claw.common.model.BaseEntity;
import com.claw.modules.user.enums.GenderEnum;
import com.claw.modules.user.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 * 对应数据库表：users
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "users", 
       indexes = {
           @Index(name = "idx_username", columnList = "username"),
           @Index(name = "idx_email", columnList = "email"),
           @Index(name = "idx_status", columnList = "status"),
           @Index(name = "idx_created_at", columnList = "created_at")
       })
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 邮箱
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 密码哈希
     */
    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * 密码盐值
     */
    @JsonIgnore
    @Column(name = "salt", nullable = false, length = 50)
    private String salt;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 性别：0-未知, 1-男, 2-女
     */
    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private GenderEnum gender = GenderEnum.UNKNOWN;

    /**
     * 出生日期
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * 技术栈，JSON数组存储
     */
    @Column(name = "tech_stack", length = 500)
    @Convert(converter = StringListConverter.class)
    private List<String> techStack;

    /**
     * 工作年限
     */
    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    /**
     * 目标岗位
     */
    @Column(name = "target_position", length = 50)
    private String targetPosition;

    /**
     * 当前水平
     */
    @Column(name = "current_level", length = 50)
    private String currentLevel;

    /**
     * 每日学习时间（分钟）
     */
    @Column(name = "daily_study_time")
    private Integer dailyStudyTime = 30;

    /**
     * 学习目标
     */
    @Column(name = "study_goal", columnDefinition = "TEXT")
    private String studyGoal;

    /**
     * 偏好难度
     */
    @Column(name = "preferred_difficulty", length = 20)
    private String preferredDifficulty = "medium";

    /**
     * 用户状态：0-禁用, 1-正常, 2-锁定
     */
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private UserStatusEnum status = UserStatusEnum.NORMAL;

    /**
     * 邮箱已验证
     */
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    /**
     * 手机已验证
     */
    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    /**
     * 总学习时长（分钟）
     */
    @Column(name = "total_study_time")
    private Integer totalStudyTime = 0;

    /**
     * 总答题数
     */
    @Column(name = "total_questions")
    private Integer totalQuestions = 0;

    /**
     * 正确率
     */
    @Column(name = "correct_rate", precision = 5, scale = 2)
    private Double correctRate = 0.0;

    /**
     * 连续学习天数
     */
    @Column(name = "continuous_days")
    private Integer continuousDays = 0;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // 转换器类
    @Converter
    public static class StringListConverter implements AttributeConverter<List<String>, String> {
        @Override
        public String convertToDatabaseColumn(List<String> list) {
            if (list == null || list.isEmpty()) {
                return null;
            }
            return String.join(",", list);
        }

        @Override
        public List<String> convertToEntityAttribute(String dbData) {
            if (dbData == null || dbData.trim().isEmpty()) {
                return List.of();
            }
            return List.of(dbData.split(","));
        }
    }

    /**
     * 更新最后登录时间
     */
    public void updateLastLoginTime() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 更新学习统计数据
     * @param isCorrect 是否答题正确
     * @param timeSpent 答题耗时（秒）
     */
    public void updateStudyStats(boolean isCorrect, int timeSpent) {
        this.totalQuestions++;
        if (isCorrect) {
            // 更新正确率
            if (this.correctRate == null) {
                this.correctRate = 100.0;
            } else {
                int correctCount = (int) (this.totalQuestions * this.correctRate / 100.0);
                correctCount++;
                this.correctRate = (double) correctCount / this.totalQuestions * 100.0;
            }
        }
        this.totalStudyTime += timeSpent / 60; // 转换为分钟
    }

    /**
     * 增加连续学习天数
     */
    public void incrementContinuousDays() {
        this.continuousDays++;
    }

    /**
     * 重置连续学习天数
     */
    public void resetContinuousDays() {
        this.continuousDays = 0;
    }

    /**
     * 判断用户是否活跃
     */
    public boolean isActive() {
        return this.status == UserStatusEnum.NORMAL;
    }

    /**
     * 判断用户是否被锁定
     */
    public boolean isLocked() {
        return this.status == UserStatusEnum.LOCKED;
    }

    /**
     * 判断用户是否被禁用
     */
    public boolean isDisabled() {
        return this.status == UserStatusEnum.DISABLED;
    }

    /**
     * 启用用户账户
     */
    public void enable() {
        this.status = UserStatusEnum.NORMAL;
    }

    /**
     * 禁用用户账户
     */
    public void disable() {
        this.status = UserStatusEnum.DISABLED;
    }

    /**
     * 锁定用户账户
     */
    public void lock() {
        this.status = UserStatusEnum.LOCKED;
    }

    /**
     * 解锁用户账户
     */
    public void unlock() {
        this.status = UserStatusEnum.NORMAL;
    }
}