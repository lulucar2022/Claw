package com.claw.modules.question.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionListDTO {
    private Long questionId;
    private String title;
    private String content;
    private String questionType;
    private String difficulty;
    private Long categoryId;
    private List<String> tags;
    private Integer viewCount;
    private Integer answerCount;
    private Double correctRate;
    private Integer favoriteCount;
    private Double hotScore;
    private LocalDateTime createdAt;
    private Boolean isFavorite;
}