package com.claw.modules.user.entity;

import com.claw.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 用户会话实体类
 * 对应数据库表：user_sessions
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_sessions", 
       indexes = {
           @Index(name = "idx_user_id", columnList = "user_id"),
           @Index(name = "idx_session_token", columnList = "session_token"),
           @Index(name = "idx_expires_at", columnList = "expires_at"),
           @Index(name = "idx_is_active", columnList = "is_active")
       })
public class UserSession extends BaseEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 会话令牌
     */
    @Column(name = "session_token", nullable = false, length = 255)
    private String sessionToken;

    /**
     * 刷新令牌
     */
    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    /**
     * 设备信息
     */
    @Column(name = "device_info", length = 500)
    private String deviceInfo;

    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * 用户代理
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * 是否活跃
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * 过期时间
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * 最后活动时间
     */
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    // 与User实体的多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    /**
     * 更新最后活动时间
     */
    public void updateLastActivityTime() {
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * 判断会话是否过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    /**
     * 使会话失效
     */
    public void invalidate() {
        this.isActive = false;
        this.expiresAt = LocalDateTime.now();
    }

    /**
     * 判断会话是否有效
     */
    public boolean isValid() {
        return Boolean.TRUE.equals(this.isActive) && !isExpired();
    }

    /**
     * 延长会话时间
     * @param minutes 延长时间（分钟）
     */
    public void extendSession(int minutes) {
        this.expiresAt = LocalDateTime.now().plusMinutes(minutes);
    }

    /**
     * 创建新的刷新令牌
     * @param token 刷新令牌
     * @param expirationMinutes 过期时间（分钟）
     */
    public void setRefreshToken(String token, int expirationMinutes) {
        this.refreshToken = token;
        // 刷新令牌的过期时间通常比会话时间长
        this.expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
    }
}