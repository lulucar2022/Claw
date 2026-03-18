package com.claw.modules.user.controller;

import com.claw.common.response.ApiResponse;
import com.claw.modules.user.dto.AuthResponse;
import com.claw.modules.user.dto.LoginRequest;
import com.claw.modules.user.dto.RefreshTokenRequest;
import com.claw.modules.user.dto.RegisterRequest;
import com.claw.modules.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证控制器
 * 对应API文档中的认证模块接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "用户认证", description = "用户注册、登录、Token刷新等认证相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        
        log.info("用户注册请求: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        
        return ResponseEntity.ok(ApiResponse.success(response, "注册成功"));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        
        log.info("用户登录请求: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        
        return ResponseEntity.ok(ApiResponse.success(response, "登录成功"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用刷新令牌获取新的访问令牌")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        
        log.info("刷新Token请求");
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        
        return ResponseEntity.ok(ApiResponse.success(response, "刷新成功"));
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "用户退出登录，使当前会话失效")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(description = "Bearer访问令牌", required = true) 
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        
        log.info("用户退出登录请求");
        authService.logout(authorizationHeader);
        
        return ResponseEntity.ok(ApiResponse.success("退出登录成功"));
    }

    @GetMapping("/validate")
    @Operation(summary = "验证Token", description = "验证访问令牌是否有效")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(
            @Parameter(description = "Bearer访问令牌", required = true) 
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        
        log.info("验证Token请求");
        String token = extractToken(authorizationHeader);
        boolean isValid = authService.validateToken(token);
        
        return ResponseEntity.ok(ApiResponse.success(isValid, "验证完成"));
    }

    @GetMapping("/current-user")
    @Operation(summary = "获取当前用户", description = "获取当前认证用户的信息")
    public ResponseEntity<ApiResponse<Long>> getCurrentUserId() {
        
        log.info("获取当前用户ID请求");
        Long userId = authService.getCurrentUserId();
        
        return ResponseEntity.ok(ApiResponse.success(userId, "获取成功"));
    }

    /**
     * 从Authorization头中提取令牌
     */
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Authorization头格式错误");
    }
}