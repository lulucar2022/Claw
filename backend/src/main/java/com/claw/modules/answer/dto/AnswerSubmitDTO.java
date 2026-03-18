package com.claw.modules.answer.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
public class AnswerSubmitDTO {
    @NotBlank(message = "用户答案不能为空")
    private String userAnswer;
    
    @NotNull(message = "题目ID不能为空")
    private Long questionId;
    
    private Integer timeSpent;
    private Integer confidenceLevel;
    private String notes;
    private String codeSubmission;
    private String sessionId;
    
    // 用于代码题的额外信息
    private Map<String, Object> testCases;
    private String language;
}