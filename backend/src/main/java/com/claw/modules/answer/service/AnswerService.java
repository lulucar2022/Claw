package com.claw.modules.answer.service;

import com.claw.modules.answer.entity.UserAnswer;
import com.claw.modules.answer.repository.UserAnswerRepository;
import com.claw.modules.answer.dto.*;
import com.claw.modules.question.entity.Question;
import com.claw.modules.question.repository.QuestionRepository;
import com.claw.common.exception.DataNotFoundException;
import com.claw.common.security.JwtTokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerService {
    
    private final UserAnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AnswerService(UserAnswerRepository answerRepository,
                        QuestionRepository questionRepository,
                        JwtTokenProvider jwtTokenProvider) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    /**
     * 提交答案
     */
    @Transactional
    public AnswerResultDTO submitAnswer(String token, AnswerSubmitDTO submitDTO) {
        // 从token中获取用户ID
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        
        // 获取题目信息
        Question question = questionRepository.findById(submitDTO.getQuestionId())
                .orElseThrow(() -> new DataNotFoundException("题目不存在"));
        
        // 判断答案是否正确
        boolean isCorrect = false;
        Double score = 0.0;
        
        if ("single".equals(question.getQuestionType()) || 
            "multiple".equals(question.getQuestionType())) {
            // 选择题
            isCorrect = question.getCorrectAnswer().equals(submitDTO.getUserAnswer());
            score = isCorrect ? 10.0 : 0.0;
        } else if ("code".equals(question.getQuestionType())) {
            // 代码题 - 这里简化处理，实际应该执行代码并判断测试用例
            isCorrect = checkCodeAnswer(submitDTO.getCodeSubmission(), question.getTestCases());
            score = isCorrect ? 10.0 : calculatePartialScore(submitDTO.getTestCases());
        } else {
            // 其他类型题目
            isCorrect = checkTextAnswer(submitDTO.getUserAnswer(), question.getCorrectAnswer());
            score = isCorrect ? 10.0 : 5.0; // 部分正确给5分
        }
        
        // 创建答题记录
        UserAnswer answer = new UserAnswer();
        answer.setUserId(userId);
        answer.setQuestionId(submitDTO.getQuestionId());
        answer.setUserAnswer(submitDTO.getUserAnswer());
        answer.setIsCorrect(isCorrect);
        answer.setScore(score);
        answer.setTimeSpent(submitDTO.getTimeSpent());
        answer.setCodeSubmission(submitDTO.getCodeSubmission());
        answer.setConfidenceLevel(submitDTO.getConfidenceLevel());
        answer.setNotes(submitDTO.getNotes());
        answer.setSessionId(submitDTO.getSessionId());
        
        // 设置测试结果（如果是代码题）
        if ("code".equals(question.getQuestionType())) {
            Map<String, Object> testResults = new HashMap<>();
            testResults.put("executionTime", 120);
            testResults.put("memoryUsage", 256);
            testResults.put("testCasesPassed", isCorrect ? 10 : 5);
            testResults.put("totalTestCases", 10);
            answer.setTestResults(testResults);
        }
        
        // 设置复习时间（如果答错）
        if (!isCorrect) {
            answer.setNextReviewAt(LocalDateTime.now().plusDays(1));
        }
        
        UserAnswer savedAnswer = answerRepository.save(answer);
        
        // 更新题目的统计信息
        updateQuestionStatistics(question, isCorrect);
        
        // 返回答题结果
        return convertToAnswerResultDTO(savedAnswer, question);
    }
    
    /**
     * 获取用户的答题记录
     */
    @Transactional(readOnly = true)
    public Page<AnswerResultDTO> getUserAnswers(String token, Long questionId, Pageable pageable) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        
        Page<UserAnswer> answerPage;
        if (questionId != null) {
            answerPage = answerRepository.findByUserIdAndQuestionId(userId, questionId, pageable);
        } else {
            answerPage = answerRepository.findByUserId(userId, pageable);
        }
        
        return answerPage.map(answer -> {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new DataNotFoundException("题目不存在"));
            return convertToAnswerResultDTO(answer, question);
        });
    }
    
    /**
     * 获取题目答题统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getQuestionAnswerStats(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new DataNotFoundException("题目不存在"));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("questionId", questionId);
        stats.put("title", question.getTitle());
        stats.put("answerCount", question.getAnswerCount());
        stats.put("correctCount", question.getCorrectCount());
        stats.put("wrongCount", question.getWrongCount());
        stats.put("correctRate", question.getAnswerCount() > 0 ? 
                (double) question.getCorrectCount() / question.getAnswerCount() * 100 : 0);
        stats.put("avgTimeSpent", question.getAvgTimeSpent());
        stats.put("viewCount", question.getViewCount());
        
        return stats;
    }
    
    /**
     * 获取用户答题统计
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getUserAnswerStats(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("totalAnswers", answerRepository.count());
        stats.put("correctAnswers", answerRepository.countCorrectAnswersByUserId(userId));
        stats.put("wrongAnswers", answerRepository.countWrongAnswersByUserId(userId));
        stats.put("totalTimeSpent", answerRepository.sumTimeSpentByUserId(userId));
        
        // 计算各分类的答题情况
        List<Object[]> categoryStats = answerRepository.findCategoryStatsByUserId(userId);
        List<Map<String, Object>> categoryStatsList = new ArrayList<>();
        for (Object[] row : categoryStats) {
            Map<String, Object> categoryStat = new HashMap<>();
            categoryStat.put("categoryId", row[0]);
            categoryStat.put("answerCount", row[1]);
            categoryStat.put("correctCount", row[2]);
            categoryStatsList.add(categoryStat);
        }
        stats.put("categoryStats", categoryStatsList);
        
        return stats;
    }
    
    /**
     * 获取需要复习的题目
     */
    @Transactional(readOnly = true)
    public List<AnswerResultDTO> getAnswersNeedReview(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        
        List<UserAnswer> answers = answerRepository.findAnswersNeedReview(userId, LocalDateTime.now());
        List<AnswerResultDTO> result = new ArrayList<>();
        
        for (UserAnswer answer : answers) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new DataNotFoundException("题目不存在"));
            result.add(convertToAnswerResultDTO(answer, question));
        }
        
        return result;
    }
    
    /**
     * 标记答案已复习
     */
    @Transactional
    public void markAnswerReviewed(Long answerId, String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        
        UserAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new DataNotFoundException("答题记录不存在"));
        
        if (!answer.getUserId().equals(userId)) {
            throw new SecurityException("无权操作");
        }
        
        answer.setIsReviewed(true);
        answer.setReviewedAt(LocalDateTime.now());
        answer.setReviewCount(answer.getReviewCount() + 1);
        
        // 根据艾宾浩斯记忆曲线设置下次复习时间
        int reviewCount = answer.getReviewCount();
        long days = calculateNextReviewDays(reviewCount);
        answer.setNextReviewAt(LocalDateTime.now().plusDays(days));
        
        answerRepository.save(answer);
    }
    
    /**
     * 判断代码题答案
     */
    private boolean checkCodeAnswer(String userCode, Object testCases) {
        // 简化处理：实际应该编译执行代码并判断测试用例
        // 这里假设只要提交了代码就认为正确
        return userCode != null && !userCode.trim().isEmpty();
    }
    
    /**
     * 计算代码题部分分数
     */
    private Double calculatePartialScore(Map<String, Object> testCases) {
        // 简化处理：根据通过的测试用例比例计算分数
        if (testCases == null) return 0.0;
        return 5.0; // 部分正确给5分
    }
    
    /**
     * 判断文本题答案
     */
    private boolean checkTextAnswer(String userAnswer, String correctAnswer) {
        if (userAnswer == null || correctAnswer == null) return false;
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }
    
    /**
     * 更新题目统计信息
     */
    private void updateQuestionStatistics(Question question, boolean isCorrect) {
        question.setAnswerCount(question.getAnswerCount() + 1);
        if (isCorrect) {
            question.setCorrectCount(question.getCorrectCount() + 1);
        } else {
            question.setWrongCount(question.getWrongCount() + 1);
        }
        
        // 计算热度分数
        double hotScore = question.getViewCount() * 0.3 + 
                         question.getAnswerCount() * 0.4 + 
                         question.getFavoriteCount() * 0.3;
        question.setHotScore(hotScore);
        
        questionRepository.save(question);
    }
    
    /**
     * 计算下次复习天数（艾宾浩斯记忆曲线）
     */
    private long calculateNextReviewDays(int reviewCount) {
        switch (reviewCount) {
            case 1: return 1;  // 1天后
            case 2: return 3;  // 3天后
            case 3: return 7;  // 7天后
            case 4: return 14; // 14天后
            case 5: return 30; // 30天后
            default: return 60; // 之后每60天一次
        }
    }
    
    /**
     * 转换答题记录为结果DTO
     */
    private AnswerResultDTO convertToAnswerResultDTO(UserAnswer answer, Question question) {
        AnswerResultDTO dto = new AnswerResultDTO();
        dto.setAnswerId(answer.getId());
        dto.setQuestionId(answer.getQuestionId());
        dto.setIsCorrect(answer.getIsCorrect());
        dto.setUserAnswer(answer.getUserAnswer());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setScore(answer.getScore());
        dto.setTimeSpent(answer.getTimeSpent());
        dto.setAnswerExplanation(question.getAnswerExplanation());
        
        // 设置代码题结果
        if ("code".equals(question.getQuestionType())) {
            dto.setExecutionTime(120);
            dto.setMemoryUsage(256);
        }
        
        // 设置分析信息（简化处理）
        List<String> weakPoints = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        
        if (!answer.getIsCorrect()) {
            weakPoints.add("知识点掌握不牢固");
            suggestions.add("建议复习相关知识点");
        }
        
        dto.setWeakPoints(weakPoints);
        dto.setSuggestions(suggestions);
        dto.setNextReviewTime(answer.getNextReviewAt());
        
        // 设置题目信息
        dto.setQuestionTitle(question.getTitle());
        dto.setQuestionContent(question.getContent());
        dto.setQuestionType(question.getQuestionType());
        dto.setDifficulty(question.getDifficulty());
        
        return dto;
    }
}