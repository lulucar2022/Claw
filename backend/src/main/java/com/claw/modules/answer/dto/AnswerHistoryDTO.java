package com.claw.modules.answer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerHistoryDTO {
    private Long answerId;
    private Long questionId;
    private String questionTitle;
    private String questionType;
    private String difficulty;
    private String userAnswer;
    private Boolean isCorrect;
    private Double score;
    private Integer timeSpent;
    private LocalDateTime answeredAt;
    private LocalDateTime nextReviewAt;
}