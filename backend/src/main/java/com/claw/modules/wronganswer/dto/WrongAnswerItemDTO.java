package com.claw.modules.wronganswer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WrongAnswerItemDTO {
    private Long itemId;
    private Long collectionId;
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private String questionType;
    private String difficulty;
    private String categoryName;
    private String wrongReason;
    private String correctUnderstanding;
    private String notes;
    private Integer masteryLevel;
    private Integer errorCount;
    private LocalDateTime lastErrorAt;
    private Integer reviewCount;
    private Boolean isMastered;
    private LocalDateTime masteredAt;
    private LocalDateTime addedAt;
    private LocalDateTime lastReviewedAt;
    private LocalDateTime nextReviewAt;
}