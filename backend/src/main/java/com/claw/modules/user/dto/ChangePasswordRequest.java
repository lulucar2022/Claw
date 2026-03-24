package com.claw.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改密码请求DTO
 */
@Data
@Schema(description = "修改密码请求")
public class ChangePasswordRequest {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码", example = "oldpassword123", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", example = "newpassword456", required = true)
    private String newPassword;
}