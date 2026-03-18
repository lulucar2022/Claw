package com.claw.modules.question.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private Long categoryId;
    private String difficulty;
    private String tags;
    private String keyword;
    private String sortBy;
    private String sortOrder;
    
    // 分页参数
    private Integer page = 1;
    private Integer size = 20;
}