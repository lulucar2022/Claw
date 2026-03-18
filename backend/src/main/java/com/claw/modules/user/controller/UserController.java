package com.claw.modules.user.controller;

import com.claw.common.response.ApiResponse;
import com.claw.modules.user.dto.UpdateProfileRequest;
import com.claw.modules.user.entity.User;
import com.claw.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 * 对应API文档中的用户信息模块接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "用户信息", description = "用户信息查询、更新等接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "获取用户详细信息", description = "获取当前用户的详细信息")
    public ResponseEntity<ApiResponse<User>> getProfile() {
        
        log.info("获取用户详细信息请求");
        User user = userService.getCurrentUserWithProfile();
        
        return ResponseEntity.ok(ApiResponse.success(user, "获取成功"));
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户信息", description = "更新当前用户的个人信息")
    public ResponseEntity<ApiResponse<User>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        
        log.info("更新用户信息请求");
        User updatedUser = userService.updateProfile(request);
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "更新成功"));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取指定用户信息", description = "根据用户ID获取用户信息（需要管理员权限）")
    public ResponseEntity<ApiResponse<User>> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        
        log.info("获取指定用户信息请求: {}", userId);
        User user = userService.getUserById(userId);
        
        return ResponseEntity.ok(ApiResponse.success(user, "获取成功"));
    }

    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表（需要管理员权限）")
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(
            @Parameter(description = "分页参数")
            @PageableDefault(size = 20) Pageable pageable,
            
            @Parameter(description = "搜索关键词（用户名或邮箱）")
            @RequestParam(required = false) String keyword,
            
            @Parameter(description = "用户状态：0-禁用, 1-正常, 2-锁定")
            @RequestParam(required = false) Integer status) {
        
        log.info("分页查询用户列表请求: keyword={}, status={}", keyword, status);
        Page<User> users = userService.getUsers(pageable, keyword, status);
        
        return ResponseEntity.ok(ApiResponse.success(users, "查询成功"));
    }

    @PutMapping("/{userId}/status")
    @Operation(summary = "更新用户状态", description = "更新用户状态（需要管理员权限）")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId,
            
            @Parameter(description = "目标状态：0-禁用, 1-正常, 2-锁定", required = true)
            @RequestParam Integer status) {
        
        log.info("更新用户状态请求: userId={}, status={}", userId, status);
        userService.updateUserStatus(userId, status);
        
        return ResponseEntity.ok(ApiResponse.success("状态更新成功"));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "软删除用户（需要管理员权限）")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        
        log.info("删除用户请求: {}", userId);
        userService.deleteUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
    }

    @PutMapping("/{userId}/restore")
    @Operation(summary = "恢复用户", description = "恢复已删除的用户（需要管理员权限）")
    public ResponseEntity<ApiResponse<Void>> restoreUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        
        log.info("恢复用户请求: {}", userId);
        userService.restoreUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success("用户恢复成功"));
    }

    @GetMapping("/stats/summary")
    @Operation(summary = "获取用户统计摘要", description = "获取系统用户统计摘要（需要管理员权限）")
    public ResponseEntity<ApiResponse<Object>> getUserStatsSummary() {
        
        log.info("获取用户统计摘要请求");
        Object stats = userService.getUserStatsSummary();
        
        return ResponseEntity.ok(ApiResponse.success(stats, "获取成功"));
    }

    @GetMapping("/search/by-tech")
    @Operation(summary = "根据技术栈搜索用户", description = "根据技术栈搜索用户")
    public ResponseEntity<ApiResponse<Page<User>>> searchUsersByTech(
            @Parameter(description = "技术栈关键词")
            @RequestParam String techKeyword,
            
            @Parameter(description = "分页参数")
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("根据技术栈搜索用户请求: techKeyword={}", techKeyword);
        Page<User> users = userService.searchUsersByTech(techKeyword, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "搜索成功"));
    }

    @GetMapping("/rankings/study")
    @Operation(summary = "学习排行榜", description = "获取用户学习排行榜")
    public ResponseEntity<ApiResponse<Page<Object>>> getStudyRankings(
            @Parameter(description = "分页参数")
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("获取学习排行榜请求");
        Page<Object> rankings = userService.getStudyRankings(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(rankings, "获取成功"));
    }

    @GetMapping("/stats/daily-new")
    @Operation(summary = "获取每日新增用户统计", description = "获取最近7天每日新增用户统计（需要管理员权限）")
    public ResponseEntity<ApiResponse<Object>> getDailyNewUserStats() {
        
        log.info("获取每日新增用户统计请求");
        Object stats = userService.getDailyNewUserStats();
        
        return ResponseEntity.ok(ApiResponse.success(stats, "获取成功"));
    }
}