package com.claw.modules.user.repository;

import com.claw.modules.user.entity.User;
import com.claw.modules.user.enums.UserStatusEnum;
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
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据用户名或邮箱查找用户
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * 根据状态查找用户列表
     */
    List<User> findByStatus(UserStatusEnum status);

    /**
     * 分页查找用户
     */
    Page<User> findAllByDeletedAtIsNull(Pageable pageable);

    /**
     * 根据用户名模糊搜索
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.username LIKE %:keyword%")
    Page<User> searchByUsername(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据邮箱模糊搜索
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.email LIKE %:keyword%")
    Page<User> searchByEmail(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据技术栈包含查找用户
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.techStack LIKE %:tech%")
    List<User> findByTechStackContaining(@Param("tech") String tech);

    /**
     * 统计活跃用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL AND u.status = :status")
    long countByStatus(@Param("status") UserStatusEnum status);

    /**
     * 统计今日新增用户
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL AND DATE(u.createdAt) = DATE(:today)")
    long countTodayNewUsers(@Param("today") LocalDateTime today);

    /**
     * 根据最后登录时间范围查找用户
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.lastLoginAt BETWEEN :start AND :end")
    List<User> findByLastLoginTimeRange(@Param("start") LocalDateTime start, 
                                        @Param("end") LocalDateTime end);

    /**
     * 根据连续学习天数查找用户
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.continuousDays >= :minDays")
    List<User> findByContinuousDaysGreaterThanEqual(@Param("minDays") Integer minDays);

    /**
     * 根据学习时长范围查找用户
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.totalStudyTime BETWEEN :minTime AND :maxTime")
    List<User> findByTotalStudyTimeRange(@Param("minTime") Integer minTime, 
                                         @Param("maxTime") Integer maxTime);

    /**
     * 更新用户最后登录时间
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.id = :userId")
    int updateLastLoginTime(@Param("userId") Long userId, 
                           @Param("lastLoginAt") LocalDateTime lastLoginAt);

    /**
     * 更新用户状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
    int updateStatus(@Param("userId") Long userId, 
                     @Param("status") UserStatusEnum status);

    /**
     * 更新用户密码
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.passwordHash = :passwordHash, u.salt = :salt WHERE u.id = :userId")
    int updatePassword(@Param("userId") Long userId, 
                      @Param("passwordHash") String passwordHash, 
                      @Param("salt") String salt);

    /**
     * 增加用户学习时长
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.totalStudyTime = u.totalStudyTime + :addMinutes WHERE u.id = :userId")
    int incrementTotalStudyTime(@Param("userId") Long userId, 
                               @Param("addMinutes") Integer addMinutes);

    /**
     * 增加用户答题数
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.totalQuestions = u.totalQuestions + 1 WHERE u.id = :userId")
    int incrementTotalQuestions(@Param("userId") Long userId);

    /**
     * 更新用户正确率
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.correctCount = u.correctCount + :addCount, u.correctRate = (u.correctCount + :addCount) * 100.0 / u.totalQuestions WHERE u.id = :userId")
    int updateCorrectRate(@Param("userId") Long userId, 
                         @Param("addCount") Integer addCount);

    /**
     * 批量软删除用户
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deletedAt = :deletedAt WHERE u.id IN :userIds")
    int batchSoftDelete(@Param("userIds") List<Long> userIds, 
                       @Param("deletedAt") LocalDateTime deletedAt);

    /**
     * 恢复已删除的用户
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deletedAt = NULL WHERE u.id = :userId")
    int restoreUser(@Param("userId") Long userId);

    /**
     * 判断用户名是否存在
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND u.deletedAt IS NULL")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 判断邮箱是否存在
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 判断手机号是否存在
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.phone = :phone AND u.deletedAt IS NULL")
    boolean existsByPhone(@Param("phone") String phone);

    /**
     * 根据技术栈统计用户分布
     */
    @Query(value = "SELECT tech, COUNT(*) as count FROM (" +
                   "SELECT DISTINCT u.id, SUBSTRING_INDEX(SUBSTRING_INDEX(u.tech_stack, ',', n.n), ',', -1) as tech " +
                   "FROM users u " +
                   "CROSS JOIN (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) n " +
                   "WHERE u.deleted_at IS NULL AND u.tech_stack IS NOT NULL AND " +
                   "CHAR_LENGTH(u.tech_stack) - CHAR_LENGTH(REPLACE(u.tech_stack, ',', '')) >= n.n - 1" +
                   ") t GROUP BY tech ORDER BY count DESC",
           nativeQuery = true)
    List<Object[]> countUsersByTechnology();

    /**
     * 获取用户学习排名
     */
    @Query("SELECT u.id, u.username, u.totalStudyTime, u.correctRate, u.continuousDays " +
           "FROM User u WHERE u.deletedAt IS NULL " +
           "ORDER BY u.totalStudyTime DESC, u.correctRate DESC, u.continuousDays DESC")
    List<Object[]> findUserStudyRanking(Pageable pageable);

    /**
     * 根据技术栈和工作年限统计用户
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND " +
           "(:techStack IS NULL OR u.techStack LIKE %:techStack%) AND " +
           "(:minExperience IS NULL OR u.experienceYears >= :minExperience) AND " +
           "(:maxExperience IS NULL OR u.experienceYears <= :maxExperience)")
    Page<User> findByTechAndExperience(@Param("techStack") String techStack,
                                       @Param("minExperience") Integer minExperience,
                                       @Param("maxExperience") Integer maxExperience,
                                       Pageable pageable);
}