package com.claw.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户个人资料请求DTO
 */
@Data
@Schema(description = "更新用户个人资料请求")
public class UpdateProfileRequest {

    @Schema(description = "用户昵称", example = "技术达人")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "邮箱", example = "user@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "个人简介", example = "热爱编程，专注后端开发")
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    private String bio;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "技术栈（逗号分隔）", example = "Java,Spring,MySQL")
    @Size(max = 200, message = "技术栈长度不能超过200个字符")
    private String techStack;

    @Schema(description = "工作年限", example = "3")
    private Integer experienceYears;

    @Schema(description = "目标职位", example = "高级Java开发工程师")
    @Size(max = 100, message = "目标职位长度不能超过100个字符")
    private String targetPosition;

    @Schema(description = "当前水平", example = "中级")
    @Size(max = 50, message = "当前水平长度不能超过50个字符")
    private String currentLevel;

    @Schema(description = "每日学习时间（分钟）", example = "120")
    private Integer dailyStudyTime;

    @Schema(description = "偏好难度", example = "中等")
    @Size(max = 50, message = "偏好难度长度不能超过50个字符")
    private String preferredDifficulty;

    @Schema(description = "学习目标", example = "掌握Spring Cloud微服务架构")
    @Size(max = 200, message = "学习目标长度不能超过200个字符")
    private String studyGoal;

    @Schema(description = "教育背景", example = "本科")
    @Size(max = 100, message = "教育背景长度不能超过100个字符")
    private String educationLevel;

    @Schema(description = "毕业院校", example = "某某大学")
    @Size(max = 100, message = "毕业院校长度不能超过100个字符")
    private String schoolName;

    @Schema(description = "专业", example = "计算机科学与技术")
    @Size(max = 100, message = "专业长度不能超过100个字符")
    private String major;

    @Schema(description = "毕业年份", example = "2020")
    private Integer graduationYear;

    @Schema(description = "目标考试", example = "软考高级")
    @Size(max = 100, message = "目标考试长度不能超过100个字符")
    private String targetExam;

    @Schema(description = "考试日期", example = "2024-11-01")
    private String examDate;

    @Schema(description = "学习计划", example = "每天学习2小时，周末4小时")
    @Size(max = 500, message = "学习计划长度不能超过500个字符")
    private String studyPlan;
}