package com.claw.modules.wronganswer.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CollectionCreateDTO {
    @NotBlank(message = "错题集名称不能为空")
    private String name;
    
    private String description;
    private List<String> tags;
    private Long categoryId;
    private Boolean isPublic = false;
    private Integer reviewIntervalDays = 7;
    private Integer dailyReviewLimit = 10;
}