package com.claw.modules.user.repository;

import com.claw.modules.user.entity.UserSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户会话数据访问层
 */
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    /**
     * 根据会话令牌查找会话
     */
    Optional<UserSession> findBySessionToken(String sessionToken);

    /**
     * 根据刷新令牌查找会话
     */
    Optional<UserSession> findByRefreshToken(String refreshToken);

    /**
     * 根据用户ID查找活跃会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.isActive = true AND us.expiresAt > :now")
    List<UserSession> findActiveSessionsByUserId(@Param("userId") Long userId, 
                                                @Param("now") LocalDateTime now);

    /**
     * 根据用户ID分页查找会话
     */
    Page<UserSession> findByUserId(Long userId, Pageable pageable);

    /**
     * 查找过期会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.expiresAt < :now")
    List<UserSession> findExpiredSessions(@Param("now") LocalDateTime now);

    /**
     * 查找所有活跃会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.isActive = true AND us.expiresAt > :now")
    List<UserSession> findAllActiveSessions(@Param("now") LocalDateTime now);

    /**
     * 根据IP地址查找会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.ipAddress = :ipAddress")
    List<UserSession> findByIpAddress(@Param("ipAddress") String ipAddress);

    /**
     * 根据设备信息查找会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.deviceInfo LIKE %:deviceInfo%")
    List<UserSession> findByDeviceInfoContaining(@Param("deviceInfo") String deviceInfo);

    /**
     * 根据最后活动时间范围查找会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.lastActivityAt BETWEEN :start AND :end")
    List<UserSession> findByLastActivityTimeRange(@Param("start") LocalDateTime start, 
                                                 @Param("end") LocalDateTime end);

    /**
     * 使会话失效
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.id = :sessionId")
    int invalidateSession(@Param("sessionId") Long sessionId);

    /**
     * 根据会话令牌使会话失效
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.sessionToken = :sessionToken")
    int invalidateSessionByToken(@Param("sessionToken") String sessionToken);

    /**
     * 根据用户ID使所有会话失效
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.userId = :userId")
    int invalidateAllSessionsByUserId(@Param("userId") Long userId);

    /**
     * 批量使会话失效
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.isActive = false WHERE us.id IN :sessionIds")
    int batchInvalidateSessions(@Param("sessionIds") List<Long> sessionIds);

    /**
     * 更新最后活动时间
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.lastActivityAt = :lastActivityAt WHERE us.id = :sessionId")
    int updateLastActivityTime(@Param("sessionId") Long sessionId, 
                              @Param("lastActivityAt") LocalDateTime lastActivityAt);

    /**
     * 延长会话过期时间
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.expiresAt = :newExpiresAt WHERE us.id = :sessionId")
    int extendSessionExpiration(@Param("sessionId") Long sessionId, 
                               @Param("newExpiresAt") LocalDateTime newExpiresAt);

    /**
     * 更新刷新令牌
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.refreshToken = :refreshToken, us.expiresAt = :newExpiresAt WHERE us.id = :sessionId")
    int updateRefreshToken(@Param("sessionId") Long sessionId, 
                          @Param("refreshToken") String refreshToken, 
                          @Param("newExpiresAt") LocalDateTime newExpiresAt);

    /**
     * 删除过期会话
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM UserSession us WHERE us.expiresAt < :now")
    int deleteExpiredSessions(@Param("now") LocalDateTime now);

    /**
     * 统计用户活跃会话数量
     */
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.userId = :userId AND us.isActive = true AND us.expiresAt > :now")
    long countActiveSessionsByUserId(@Param("userId") Long userId, 
                                    @Param("now") LocalDateTime now);

    /**
     * 统计所有活跃会话数量
     */
    @Query("SELECT COUNT(us) FROM UserSession us WHERE us.isActive = true AND us.expiresAt > :now")
    long countAllActiveSessions(@Param("now") LocalDateTime now);

    /**
     * 根据设备类型统计会话
     */
    @Query("SELECT us.deviceInfo, COUNT(us) as count FROM UserSession us " +
           "WHERE us.isActive = true AND us.expiresAt > :now " +
           "GROUP BY us.deviceInfo " +
           "ORDER BY count DESC")
    List<Object[]> countSessionsByDeviceType(@Param("now") LocalDateTime now);

    /**
     * 根据IP地址统计会话
     */
    @Query("SELECT us.ipAddress, COUNT(us) as count FROM UserSession us " +
           "WHERE us.isActive = true AND us.expiresAt > :now " +
           "GROUP BY us.ipAddress " +
           "HAVING COUNT(us) > 1 " +
           "ORDER BY count DESC")
    List<Object[]> countSessionsByIpAddress(@Param("now") LocalDateTime now);

    /**
     * 查找最近活动的会话
     */
    @Query("SELECT us FROM UserSession us WHERE us.isActive = true AND us.expiresAt > :now " +
           "ORDER BY us.lastActivityAt DESC")
    Page<UserSession> findRecentActiveSessions(@Param("now") LocalDateTime now, 
                                              Pageable pageable);

    /**
     * 根据用户ID和会话状态查找
     */
    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.isActive = :isActive")
    List<UserSession> findByUserIdAndIsActive(@Param("userId") Long userId, 
                                             @Param("isActive") Boolean isActive);
}