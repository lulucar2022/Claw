package com.claw.modules.question.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class QuestionDetailDTO {
    private Long questionId;
    private String title;
    private String content;
    private String questionType;
    private String difficulty;
    private Long categoryId;
    private List<String> tags;
    private List<String> keyPoints;
    private List<Map<String, Object>> options;
    private String correctAnswer;
    private String answerExplanation;
    private String codeTemplate;
    private List<Map<String, Object>> testCases;
    private String language;
    private String sourceInfo;
    private Integer viewCount;
    private Integer answerCount;
    private Integer correctCount;
    private Integer favoriteCount;
    private Integer avgTimeSpent;
    private LocalDateTime createdAt;
    private List<QuestionSimilarDTO> relatedQuestions;
    private Boolean isFavorite;
    private String userAnswer;
}