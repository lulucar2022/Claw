package com.claw.modules.answer.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class AnswerResultDTO {
    private Long answerId;
    private Long questionId;
    private Boolean isCorrect;
    private String userAnswer;
    private String correctAnswer;
    private Double score;
    private Integer timeSpent;
    private String answerExplanation;
    
    // 代码题结果
    private List<Map<String, Object>> testResults;
    private Integer executionTime;
    private Integer memoryUsage;
    
    // 分析信息
    private List<String> weakPoints;
    private List<String> suggestions;
    private LocalDateTime nextReviewTime;
    
    // 题目信息
    private String questionTitle;
    private String questionContent;
    private String questionType;
    private String difficulty;
}