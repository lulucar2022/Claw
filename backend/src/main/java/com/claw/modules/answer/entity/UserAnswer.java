package com.claw.modules.answer.entity;

import com.claw.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_answers", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_question_id", columnList = "question_id"),
    @Index(name = "idx_is_correct", columnList = "is_correct"),
    @Index(name = "idx_answered_at", columnList = "answered_at"),
    @Index(name = "idx_user_question", columnList = "user_id, question_id"),
    @Index(name = "idx_next_review_at", columnList = "next_review_at")
})
public class UserAnswer extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Column(name = "user_answer", columnDefinition = "TEXT")
    private String userAnswer;
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    @Column(name = "score", precision = 5, scale = 2)
    private Double score;
    
    @Column(name = "time_spent")
    private Integer timeSpent;
    
    @Column(name = "code_submission", columnDefinition = "TEXT")
    private String codeSubmission;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "test_results", columnDefinition = "json")
    private Map<String, Object> testResults;
    
    @Column(name = "execution_time")
    private Integer executionTime;
    
    @Column(name = "memory_usage")
    private Integer memoryUsage;
    
    @Column(name = "confidence_level")
    private Integer confidenceLevel;
    
    @Column(name = "difficulty_perception")
    private Integer difficultyPerception;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "is_reviewed")
    private Boolean isReviewed = false;
    
    @Column(name = "review_count")
    private Integer reviewCount = 0;
    
    @Column(name = "answered_at")
    private LocalDateTime answeredAt = LocalDateTime.now();
    
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
    
    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;
    
    @PrePersist
    public void prePersist() {
        if (answeredAt == null) {
            answeredAt = LocalDateTime.now();
        }
    }
}