package com.claw.common.response;

import lombok.Data;
import java.util.List;

@Data
public class PaginationResult<T> {
    private List<T> items;
    private Integer page;
    private Integer size;
    private Long total;
    private Integer totalPages;
    
    public PaginationResult(List<T> items, Integer page, Integer size, Long total, Integer totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = totalPages;
    }
}