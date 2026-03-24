package com.claw.modules.wronganswer.entity;

import com.claw.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wrong_answer_items", indexes = {
    @Index(name = "idx_collection_id", columnList = "collection_id"),
    @Index(name = "idx_question_id", columnList = "question_id"),
    @Index(name = "idx_next_review_at", columnList = "next_review_at"),
    @Index(name = "idx_mastery_level", columnList = "mastery_level")
})
public class WrongAnswerItem extends BaseEntity {
    
    @Column(name = "collection_id", nullable = false)
    private Long collectionId;
    
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    
    @Column(name = "user_answer_id", nullable = false)
    private Long userAnswerId;
    
    @Column(name = "wrong_reason", length = 200)
    private String wrongReason;
    
    @Column(name = "correct_understanding", columnDefinition = "TEXT")
    private String correctUnderstanding;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "mastery_level")
    private Integer masteryLevel = 1;
    
    @Column(name = "error_count")
    private Integer errorCount = 1;
    
    @Column(name = "last_error_at")
    private LocalDateTime lastErrorAt;
    
    @Column(name = "review_count")
    private Integer reviewCount = 0;
    
    @Column(name = "is_mastered")
    private Boolean isMastered = false;
    
    @Column(name = "mastered_at")
    private LocalDateTime masteredAt;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();
    
    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;
    
    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;
    
    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
        if (lastErrorAt == null) {
            lastErrorAt = LocalDateTime.now();
        }
    }
}