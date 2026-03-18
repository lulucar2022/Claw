package com.claw.modules.question.dto;

import lombok.Data;

@Data
public class CategoryUpdateDTO {
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private Integer sortOrder;
    private Boolean isActive;
}