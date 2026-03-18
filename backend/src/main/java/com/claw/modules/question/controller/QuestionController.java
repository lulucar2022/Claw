package com.claw.modules.question.controller;

import com.claw.modules.question.dto.*;
import com.claw.modules.question.service.QuestionService;
import com.claw.common.response.ApiResponse;
import com.claw.common.response.PaginationResult;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    
    private final QuestionService questionService;
    
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    
    /**
     * 获取题目列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResult<QuestionListDTO>>> getQuestions(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
        QuestionQueryDTO queryDTO = new QuestionQueryDTO();
        queryDTO.setCategoryId(categoryId);
        queryDTO.setDifficulty(difficulty);
        queryDTO.setTags(tags);
        queryDTO.setKeyword(keyword);
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortOrder(sortOrder);
        
        // 构建分页参数
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<QuestionListDTO> questionPage = questionService.getQuestions(queryDTO, pageable);
        
        PaginationResult<QuestionListDTO> paginationResult = new PaginationResult<>(
                questionPage.getContent(),
                questionPage.getNumber() + 1,
                questionPage.getSize(),
                questionPage.getTotalElements(),
                questionPage.getTotalPages()
        );
        
        return ResponseEntity.ok(ApiResponse.success(paginationResult));
    }
    
    /**
     * 获取题目详情
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDetailDTO>> getQuestionDetail(
            @PathVariable Long questionId) {
        QuestionDetailDTO detailDTO = questionService.getQuestionDetail(questionId);
        return ResponseEntity.ok(ApiResponse.success(detailDTO));
    }
    
    /**
     * 创建题目
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Long>> createQuestion(
            @Valid @RequestBody QuestionCreateDTO createDTO) {
        Long questionId = questionService.createQuestion(createDTO);
        return ResponseEntity.ok(ApiResponse.success(questionId));
    }
    
    /**
     * 更新题目
     */
    @PutMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Void>> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody QuestionUpdateDTO updateDTO) {
        questionService.updateQuestion(questionId, updateDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }
    
    /**
     * 删除题目
     */
    @DeleteMapping("/{questionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success());
    }
    
    /**
     * 发布题目
     */
    @PostMapping("/{questionId}/publish")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<Void>> publishQuestion(
            @PathVariable Long questionId) {
        questionService.publishQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success());
    }
    
    /**
     * 获取相似题目
     */
    @GetMapping("/{questionId}/similar")
    public ResponseEntity<ApiResponse<List<QuestionSimilarDTO>>> getSimilarQuestions(
            @PathVariable Long questionId,
            @RequestParam(required = false, defaultValue = "5") Integer count) {
        List<QuestionSimilarDTO> similarQuestions = questionService.getSimilarQuestions(questionId, count);
        return ResponseEntity.ok(ApiResponse.success(similarQuestions));
    }
}