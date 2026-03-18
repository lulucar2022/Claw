package com.claw.modules.question.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class QuestionUpdateDTO {
    private String title;
    private String content;
    private Long categoryId;
    private String questionType;
    private String difficulty;
    private List<String> tags;
    private List<String> keyPoints;
    private List<Map<String, Object>> options;
    private String correctAnswer;
    private String answerExplanation;
    private String codeTemplate;
    private List<Map<String, Object>> testCases;
    private String language;
    private String sourceInfo;
}