package com.claw.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 用户注册请求DTO
 */
@Data
@Schema(description = "用户注册请求")
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Schema(description = "用户名", example = "testuser", required = true)
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "test@example.com", required = true)
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    @Schema(description = "密码", example = "password123", required = true)
    private String password;

    @Size(min = 1, max = 50, message = "昵称长度必须在1-50个字符之间")
    @Schema(description = "昵称", example = "测试用户")
    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "技术栈", example = "[\"Java\", \"Spring\", \"MySQL\"]")
    private List<String> techStack;

    @Schema(description = "工作年限", example = "3")
    private Integer experienceYears;

    @Schema(description = "目标岗位", example = "高级开发工程师")
    private String targetPosition;

    @Schema(description = "当前水平", example = "中级")
    private String currentLevel;

    @Schema(description = "每日学习时间（分钟）", example = "60")
    private Integer dailyStudyTime = 30;

    @Schema(description = "偏好难度", example = "medium")
    private String preferredDifficulty = "medium";

    @Schema(description = "学习目标", example = "掌握Java核心技术")
    private String studyGoal;
}