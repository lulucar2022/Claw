package com.claw.modules.question.dto;

import lombok.Data;

@Data
public class QuestionSimilarDTO {
    private Long questionId;
    private String title;
    private String difficulty;
    private Integer answerCount;
    private Integer correctCount;
    private Double similarity;
}