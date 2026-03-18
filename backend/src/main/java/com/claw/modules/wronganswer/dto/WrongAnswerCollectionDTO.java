package com.claw.modules.wronganswer.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WrongAnswerCollectionDTO {
    private Long collectionId;
    private String name;
    private String description;
    private List<String> tags;
    private Long categoryId;
    private Integer questionCount;
    private Integer reviewCount;
    private Double masteryRate;
    private Boolean isPublic;
    private Integer reviewIntervalDays;
    private Integer dailyReviewLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastReviewedAt;
    private LocalDateTime nextReviewAt;
}