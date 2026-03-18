package com.claw.modules.question.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class QuestionCreateDTO {
    @NotBlank(message = "题目标题不能为空")
    private String title;
    
    @NotBlank(message = "题目内容不能为空")
    private String content;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @NotBlank(message = "题目类型不能为空")
    private String questionType;
    
    private String difficulty = "medium";
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