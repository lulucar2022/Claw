package com.claw.modules.wronganswer.entity;

import com.claw.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wrong_answer_collections", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_category_id", columnList = "category_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class WrongAnswerCollection extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "json")
    private List<String> tags;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(name = "question_count")
    private Integer questionCount = 0;
    
    @Column(name = "review_count")
    private Integer reviewCount = 0;
    
    @Column(name = "mastery_rate")
    private Double masteryRate = 0.0;
    
    @Column(name = "is_public")
    private Boolean isPublic = false;
    
    @Column(name = "review_interval_days")
    private Integer reviewIntervalDays = 7;
    
    @Column(name = "daily_review_limit")
    private Integer dailyReviewLimit = 10;
    
    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;
    
    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;
}