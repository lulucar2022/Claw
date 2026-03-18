package com.claw.modules.answer.controller;

import com.claw.modules.answer.dto.*;
import com.claw.modules.answer.service.AnswerService;
import com.claw.common.response.ApiResponse;
import com.claw.common.response.PaginationResult;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {
    
    private final AnswerService answerService;
    
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }
    
    /**
     * 提交答案
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AnswerResultDTO>> submitAnswer(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Valid @RequestBody AnswerSubmitDTO submitDTO) {
        
        // 提取token
        String token = authorization.replace("Bearer ", "");
        AnswerResultDTO result = answerService.submitAnswer(token, submitDTO);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    /**
     * 获取用户答题记录
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaginationResult<AnswerResultDTO>>> getUserAnswers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestParam(required = false) Long questionId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false, defaultValue = "answeredAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        
        String token = authorization.replace("Bearer ", "");
        
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        
        Page<AnswerResultDTO> answerPage = answerService.getUserAnswers(token, questionId, pageable);
        
        PaginationResult<AnswerResultDTO> paginationResult = new PaginationResult<>(
                answerPage.getContent(),
                answerPage.getNumber() + 1,
                answerPage.getSize(),
                answerPage.getTotalElements(),
                answerPage.getTotalPages()
        );
        
        return ResponseEntity.ok(ApiResponse.success(paginationResult));
    }
    
    /**
     * 获取题目答题统计
     */
    @GetMapping("/questions/{questionId}/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getQuestionAnswerStats(
            @PathVariable Long questionId) {
        Map<String, Object> stats = answerService.getQuestionAnswerStats(questionId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    /**
     * 获取用户答题统计
     */
    @GetMapping("/stats")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserAnswerStats(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = authorization.replace("Bearer ", "");
        Map<String, Object> stats = answerService.getUserAnswerStats(token);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    /**
     * 获取需要复习的题目
     */
    @GetMapping("/review")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<AnswerResultDTO>>> getAnswersNeedReview(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = authorization.replace("Bearer ", "");
        List<AnswerResultDTO> answers = answerService.getAnswersNeedReview(token);
        return ResponseEntity.ok(ApiResponse.success(answers));
    }
    
    /**
     * 标记答案已复习
     */
    @PostMapping("/{answerId}/review")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> markAnswerReviewed(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable Long answerId) {
        String token = authorization.replace("Bearer ", "");
        answerService.markAnswerReviewed(answerId, token);
        return ResponseEntity.ok(ApiResponse.success());
    }
}