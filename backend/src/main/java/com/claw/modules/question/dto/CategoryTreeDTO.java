package com.claw.modules.question.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryTreeDTO {
    private Long categoryId;
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private Integer level;
    private Integer sortOrder;
    private Integer questionCount;
    private Integer studyCount;
    private List<CategoryTreeDTO> children;
}