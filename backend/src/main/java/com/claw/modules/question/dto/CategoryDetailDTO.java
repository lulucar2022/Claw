package com.claw.modules.question.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDetailDTO {
    private Long categoryId;
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private Integer level;
    private Long parentId;
    private Integer sortOrder;
    private Integer questionCount;
    private Integer studyCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
}