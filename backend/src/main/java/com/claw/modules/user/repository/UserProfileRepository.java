package com.claw.modules.user.repository;

import com.claw.modules.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户扩展信息数据访问层
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * 根据用户ID查找扩展信息
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * 根据用户ID删除扩展信息
     */
    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    /**
     * 根据学历查找用户扩展信息
     */
    @Query("SELECT up FROM UserProfile up WHERE up.educationLevel = :educationLevel")
    Optional<UserProfile> findByEducationLevel(@Param("educationLevel") String educationLevel);

    /**
     * 根据学校名称模糊搜索
     */
    @Query("SELECT up FROM UserProfile up WHERE up.schoolName LIKE %:schoolName%")
    Optional<UserProfile> findBySchoolNameContaining(@Param("schoolName") String schoolName);

    /**
     * 根据专业查找
     */
    @Query("SELECT up FROM UserProfile up WHERE up.major = :major")
    Optional<UserProfile> findByMajor(@Param("major") String major);

    /**
     * 根据行业查找
     */
    @Query("SELECT up FROM UserProfile up WHERE up.industry = :industry")
    Optional<UserProfile> findByIndustry(@Param("industry") String industry);

    /**
     * 根据职位查找
     */
    @Query("SELECT up FROM UserProfile up WHERE up.jobTitle = :jobTitle")
    Optional<UserProfile> findByJobTitle(@Param("jobTitle") String jobTitle);

    /**
     * 根据工作年限范围查找
     */
    @Query("SELECT up FROM UserProfile up WHERE up.workYears BETWEEN :minYears AND :maxYears")
    Optional<UserProfile> findByWorkYearsBetween(@Param("minYears") Integer minYears, 
                                                @Param("maxYears") Integer maxYears);

    /**
     * 根据考试目标查找
     */
    @Query("SELECT up FROM UserProfile up WHERE up.examTarget = :examTarget")
    Optional<UserProfile> findByExamTarget(@Param("examTarget") String examTarget);

    /**
     * 更新技能评估
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET up.skillAssessment = :skillAssessment WHERE up.userId = :userId")
    int updateSkillAssessment(@Param("userId") Long userId, 
                             @Param("skillAssessment") String skillAssessment);

    /**
     * 更新薄弱点
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET up.weakPoints = :weakPoints WHERE up.userId = :userId")
    int updateWeakPoints(@Param("userId") Long userId, 
                        @Param("weakPoints") String weakPoints);

    /**
     * 更新优势点
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET up.strongPoints = :strongPoints WHERE up.userId = :userId")
    int updateStrongPoints(@Param("userId") Long userId, 
                          @Param("strongPoints") String strongPoints);

    /**
     * 更新偏好主题
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET up.preferredTopics = :preferredTopics WHERE up.userId = :userId")
    int updatePreferredTopics(@Param("userId") Long userId, 
                             @Param("preferredTopics") String preferredTopics);

    /**
     * 更新避免主题
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET up.avoidTopics = :avoidTopics WHERE up.userId = :userId")
    int updateAvoidTopics(@Param("userId") Long userId, 
                         @Param("avoidTopics") String avoidTopics);

    /**
     * 更新备考计划
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET up.preparationPlan = :preparationPlan WHERE up.userId = :userId")
    int updatePreparationPlan(@Param("userId") Long userId, 
                             @Param("preparationPlan") String preparationPlan);

    /**
     * 根据用户ID更新所有信息
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserProfile up SET " +
           "up.educationLevel = COALESCE(:educationLevel, up.educationLevel), " +
           "up.schoolName = COALESCE(:schoolName, up.schoolName), " +
           "up.major = COALESCE(:major, up.major), " +
           "up.graduationYear = COALESCE(:graduationYear, up.graduationYear), " +
           "up.companyName = COALESCE(:companyName, up.companyName), " +
           "up.jobTitle = COALESCE(:jobTitle, up.jobTitle), " +
           "up.workYears = COALESCE(:workYears, up.workYears), " +
           "up.industry = COALESCE(:industry, up.industry), " +
           "up.learningStyle = COALESCE(:learningStyle, up.learningStyle), " +
           "up.examTarget = COALESCE(:examTarget, up.examTarget), " +
           "up.examDate = COALESCE(:examDate, up.examDate) " +
           "WHERE up.userId = :userId")
    int updateProfileInfo(@Param("userId") Long userId,
                         @Param("educationLevel") String educationLevel,
                         @Param("schoolName") String schoolName,
                         @Param("major") String major,
                         @Param("graduationYear") java.time.Year graduationYear,
                         @Param("companyName") String companyName,
                         @Param("jobTitle") String jobTitle,
                         @Param("workYears") Integer workYears,
                         @Param("industry") String industry,
                         @Param("learningStyle") String learningStyle,
                         @Param("examTarget") String examTarget,
                         @Param("examDate") java.time.LocalDate examDate);

    /**
     * 检查用户是否存在扩展信息
     */
    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END FROM UserProfile up WHERE up.userId = :userId")
    boolean existsByUserId(@Param("userId") Long userId);
}