package com.claw.modules.wronganswer.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ItemAddDTO {
    @NotNull(message = "题目ID不能为空")
    private Long questionId;
    
    @NotNull(message = "答题记录ID不能为空")
    private Long userAnswerId;
    
    private String wrongReason;
    private String correctUnderstanding;
    private String notes;
}