package com.claw.modules.user.entity;

import com.claw.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Year;

/**
 * 用户扩展信息实体类
 * 对应数据库表：user_profiles
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_profiles", 
       indexes = {
           @Index(name = "idx_user_id", columnList = "user_id")
       })
public class UserProfile extends BaseEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * 学历
     */
    @Column(name = "education_level", length = 50)
    private String educationLevel;

    /**
     * 学校名称
     */
    @Column(name = "school_name", length = 100)
    private String schoolName;

    /**
     * 专业
     */
    @Column(name = "major", length = 100)
    private String major;

    /**
     * 毕业年份
     */
    @Column(name = "graduation_year")
    private Year graduationYear;

    /**
     * 公司名称
     */
    @Column(name = "company_name", length = 100)
    private String companyName;

    /**
     * 职位
     */
    @Column(name = "job_title", length = 100)
    private String jobTitle;

    /**
     * 工作年限
     */
    @Column(name = "work_years")
    private Integer workYears;

    /**
     * 行业
     */
    @Column(name = "industry", length = 100)
    private String industry;

    /**
     * 技能评估，JSON格式
     */
    @Column(name = "skill_assessment", columnDefinition = "JSON")
    private String skillAssessment;

    /**
     * 薄弱点，JSON格式
     */
    @Column(name = "weak_points", columnDefinition = "JSON")
    private String weakPoints;

    /**
     * 优势点，JSON格式
     */
    @Column(name = "strong_points", columnDefinition = "JSON")
    private String strongPoints;

    /**
     * 学习风格
     */
    @Column(name = "learning_style", length = 50)
    private String learningStyle;

    /**
     * 偏好主题，JSON数组
     */
    @Column(name = "preferred_topics", columnDefinition = "JSON")
    private String preferredTopics;

    /**
     * 避免主题，JSON数组
     */
    @Column(name = "avoid_topics", columnDefinition = "JSON")
    private String avoidTopics;

    /**
     * 考试目标
     */
    @Column(name = "exam_target", length = 100)
    private String examTarget;

    /**
     * 考试日期
     */
    @Column(name = "exam_date")
    private java.time.LocalDate examDate;

    /**
     * 备考计划
     */
    @Column(name = "preparation_plan", columnDefinition = "TEXT")
    private String preparationPlan;

    // 与User实体的一对一关系（可选，如果需要关联查询）
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}