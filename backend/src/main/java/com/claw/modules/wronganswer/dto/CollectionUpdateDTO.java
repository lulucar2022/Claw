package com.claw.modules.wronganswer.dto;

import lombok.Data;
import java.util.List;

@Data
public class CollectionUpdateDTO {
    private String name;
    private String description;
    private List<String> tags;
    private Long categoryId;
    private Boolean isPublic;
    private Integer reviewIntervalDays;
    private Integer dailyReviewLimit;
}