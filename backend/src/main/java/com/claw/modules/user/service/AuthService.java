package com.claw.modules.user.service;

import com.claw.common.exception.BusinessException;
import com.claw.common.exception.DataNotFoundException;
import com.claw.common.security.JwtTokenProvider;
import com.claw.modules.user.dto.*;
import com.claw.modules.user.entity.User;
import com.claw.modules.user.entity.UserSession;
import com.claw.modules.user.repository.UserRepository;
import com.claw.modules.user.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    /**
     * 用户注册
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("用户注册: {}", request.getUsername());

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw BusinessException.badRequest("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BusinessException.badRequest("邮箱已存在");
        }

        // 检查手机号是否已存在
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw BusinessException.badRequest("手机号已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname());
        user.setTechStack(request.getTechStack());
        user.setExperienceYears(request.getExperienceYears());
        
        // 生成密码盐和哈希
        String salt = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        String passwordHash = passwordEncoder.encode(request.getPassword() + salt);
        
        user.setSalt(salt);
        user.setPasswordHash(passwordHash);
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 生成JWT令牌
        String accessToken = jwtTokenProvider.generateAccessToken(savedUser.getId(), savedUser.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getId(), savedUser.getUsername());
        
        // 创建用户会话
        UserSession userSession = createUserSession(savedUser, accessToken, refreshToken);
        userSessionRepository.save(userSession);
        
        // 构建响应
        AuthResponse response = new AuthResponse();
        response.setUserId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtTokenProvider.getAccessTokenExpirationInSeconds());
        
        log.info("用户注册成功: {}", savedUser.getId());
        return response;
    }

    /**
     * 用户登录
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("用户登录: {}", request.getUsername());

        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            // 获取用户信息
            User user = (User) authentication.getPrincipal();
            
            // 更新最后登录时间
            user.updateLastLoginTime();
            userRepository.save(user);
            
            // 生成JWT令牌
            String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());
            
            // 创建用户会话
            UserSession userSession = createUserSession(user, accessToken, refreshToken);
            userSessionRepository.save(userSession);
            
            // 构建响应
            AuthResponse response = new AuthResponse();
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiresIn(jwtTokenProvider.getAccessTokenExpirationInSeconds());
            
            log.info("用户登录成功: {}", user.getId());
            return response;
            
        } catch (BadCredentialsException e) {
            log.warn("登录失败: {}", request.getUsername());
            throw BusinessException.unauthorized("用户名或密码错误");
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage(), e);
            throw BusinessException.internalServerError("登录失败，请稍后重试");
        }
    }

    /**
     * 刷新访问令牌
     */
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        log.info("刷新访问令牌");

        // 验证刷新令牌
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw BusinessException.unauthorized("刷新令牌无效或已过期");
        }

        // 解析用户ID
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        
        // 查找用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> DataNotFoundException.userNotFound(userId));
        
        // 检查用户状态
        if (!user.isActive()) {
            throw BusinessException.forbidden("用户账户已被禁用或锁定");
        }

        // 验证会话中的刷新令牌
        UserSession userSession = userSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> BusinessException.unauthorized("刷新令牌无效"));
        
        // 检查会话是否有效
        if (!userSession.isValid()) {
            throw BusinessException.unauthorized("会话已过期，请重新登录");
        }

        // 生成新的访问令牌
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, user.getUsername());
        
        // 更新会话的最后活动时间
        userSession.updateLastActivityTime();
        userSession.setSessionToken(newAccessToken);
        userSessionRepository.save(userSession);
        
        // 构建响应
        AuthResponse response = new AuthResponse();
        response.setUserId(userId);
        response.setUsername(user.getUsername());
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtTokenProvider.getAccessTokenExpirationInSeconds());
        
        log.info("访问令牌刷新成功: {}", userId);
        return response;
    }

    /**
     * 退出登录
     */
    @Transactional
    public void logout(String accessToken) {
        log.info("用户退出登录");

        // 移除Bearer前缀
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        // 使会话失效
        int updated = userSessionRepository.invalidateSessionByToken(accessToken);
        
        if (updated > 0) {
            log.info("用户退出登录成功");
        } else {
            log.warn("未找到对应的会话");
        }
    }

    /**
     * 获取当前认证用户
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw BusinessException.unauthorized("用户未认证");
        }
        
        return (User) authentication.getPrincipal();
    }

    /**
     * 获取当前用户ID
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * 创建用户会话
     */
    private UserSession createUserSession(User user, String accessToken, String refreshToken) {
        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setSessionToken(accessToken);
        userSession.setRefreshToken(refreshToken);
        userSession.setDeviceInfo("Web Browser");
        userSession.setIpAddress("127.0.0.1");
        userSession.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        userSession.setIsActive(true);
        userSession.setExpiresAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenExpirationInSeconds()));
        userSession.setLastActivityAt(LocalDateTime.now());
        return userSession;
    }

    /**
     * 修改密码
     */
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        log.info("修改密码: {}", request.getUserId());
        
        User user = userService.getUserById(request.getUserId());
        
        // 验证旧密码
        String oldPasswordHash = passwordEncoder.encode(request.getOldPassword() + user.getSalt());
        if (!user.getPasswordHash().equals(oldPasswordHash)) {
            throw BusinessException.badRequest("旧密码错误");
        }
        
        // 生成新密码哈希
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword() + user.getSalt());
        
        // 更新密码
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);
        
        // 使所有会话失效（强制重新登录）
        userSessionRepository.invalidateAllSessionsByUserId(user.getId());
        
        log.info("密码修改成功: {}", user.getId());
    }

    /**
     * 重置密码（管理员操作或忘记密码）
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        log.info("重置密码: {}", request.getUserId());
        
        User user = userService.getUserById(request.getUserId());
        
        // 验证管理员权限（这里简化处理，实际项目中需要实现权限验证）
        if (!isAdminUser()) {
            throw BusinessException.forbidden("无权重置密码");
        }
        
        // 生成新密码哈希
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword() + user.getSalt());
        
        // 更新密码
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);
        
        // 使所有会话失效（强制重新登录）
        userSessionRepository.invalidateAllSessionsByUserId(user.getId());
        
        log.info("密码重置成功: {}", user.getId());
    }

    /**
     * 验证令牌
     */
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * 从令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    /**
     * 检查是否是管理员用户（简化实现）
     */
    private boolean isAdminUser() {
        // TODO: 实现完整的权限检查逻辑
        User currentUser = getCurrentUser();
        return "admin".equals(currentUser.getUsername()) || currentUser.getId() == 1;
    }
}