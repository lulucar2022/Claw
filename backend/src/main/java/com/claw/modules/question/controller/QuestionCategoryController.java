package com.claw.modules.question.controller;

import com.claw.modules.question.dto.*;
import com.claw.modules.question.service.QuestionCategoryService;
import com.claw.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class QuestionCategoryController {
    
    private final QuestionCategoryService categoryService;
    
    public QuestionCategoryController(QuestionCategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    /**
     * 获取分类树
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryTreeDTO>>> getCategories(
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Boolean includeStatistics) {
        List<CategoryTreeDTO> categories = categoryService.getCategoryTree(parentId, includeStatistics);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    
    /**
     * 获取分类详情
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDetailDTO>> getCategoryDetail(
            @PathVariable Long categoryId) {
        CategoryDetailDTO detailDTO = categoryService.getCategoryDetail(categoryId);
        return ResponseEntity.ok(ApiResponse.success(detailDTO));
    }
    
    /**
     * 创建分类
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> createCategory(
            @Valid @RequestBody CategoryCreateDTO createDTO) {
        Long categoryId = categoryService.createCategory(createDTO);
        return ResponseEntity.ok(ApiResponse.success(categoryId));
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateDTO updateDTO) {
        categoryService.updateCategory(categoryId, updateDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}