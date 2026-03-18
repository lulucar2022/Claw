package com.claw.modules.question.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CategoryCreateDTO {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    @NotBlank(message = "分类标识不能为空")
    private String slug;
    
    private String description;
    private String iconUrl;
    
    @NotNull(message = "父分类ID不能为空")
    private Long parentId = 0L;
    
    private Integer sortOrder = 0;
}