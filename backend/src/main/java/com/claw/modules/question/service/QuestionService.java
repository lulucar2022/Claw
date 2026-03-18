package com.claw.modules.question.service;

import com.claw.modules.question.entity.Question;
import com.claw.modules.question.repository.QuestionRepository;
import com.claw.common.exception.DataNotFoundException;
import com.claw.modules.question.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository categoryRepository;
    
    public QuestionService(QuestionRepository questionRepository, 
                          QuestionCategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * 分页查询题目
     */
    @Transactional(readOnly = true)
    public Page<QuestionListDTO> getQuestions(QuestionQueryDTO queryDTO, Pageable pageable) {
        Specification<Question> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 状态过滤：只查询已发布的题目
            predicates.add(cb.equal(root.get("status"), 2)); // 2表示已发布
            
            // 分类过滤
            if (queryDTO.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), queryDTO.getCategoryId()));
            }
            
            // 难度过滤
            if (queryDTO.getDifficulty() != null) {
                predicates.add(cb.equal(root.get("difficulty"), queryDTO.getDifficulty()));
            }
            
            // 关键词搜索
            if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
                String keyword = "%" + queryDTO.getKeyword().trim() + "%";
                Predicate titlePredicate = cb.like(root.get("title"), keyword);
                Predicate contentPredicate = cb.like(root.get("content"), keyword);
                predicates.add(cb.or(titlePredicate, contentPredicate));
            }
            
            // 标签过滤
            if (queryDTO.getTags() != null && !queryDTO.getTags().isEmpty()) {
                // JSON数组包含查询
                predicates.add(cb.like(root.get("tags"), "%" + queryDTO.getTags() + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return questionRepository.findAll(spec, pageable)
                .map(this::convertToQuestionListDTO);
    }
    
    /**
     * 获取题目详情
     */
    @Transactional(readOnly = true)
    public QuestionDetailDTO getQuestionDetail(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new DataNotFoundException("题目不存在"));
        
        // 增加浏览量
        question.setViewCount(question.getViewCount() + 1);
        questionRepository.save(question);
        
        return convertToQuestionDetailDTO(question);
    }
    
    /**
     * 创建题目
     */
    @Transactional
    public Long createQuestion(QuestionCreateDTO createDTO) {
        // 验证分类是否存在
        categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("分类不存在"));
        
        Question question = new Question();
        question.setTitle(createDTO.getTitle());
        question.setContent(createDTO.getContent());
        question.setCategoryId(createDTO.getCategoryId());
        question.setQuestionType(createDTO.getQuestionType());
        question.setDifficulty(createDTO.getDifficulty());
        question.setTags(createDTO.getTags());
        question.setKeyPoints(createDTO.getKeyPoints());
        question.setOptions(createDTO.getOptions());
        question.setCorrectAnswer(createDTO.getCorrectAnswer());
        question.setAnswerExplanation(createDTO.getAnswerExplanation());
        question.setCodeTemplate(createDTO.getCodeTemplate());
        question.setTestCases(createDTO.getTestCases());
        question.setLanguage(createDTO.getLanguage());
        question.setSourceInfo(createDTO.getSourceInfo());
        question.setStatus(1); // 草稿状态
        
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion.getId();
    }
    
    /**
     * 更新题目
     */
    @Transactional
    public void updateQuestion(Long questionId, QuestionUpdateDTO updateDTO) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new DataNotFoundException("题目不存在"));
        
        // 验证分类是否存在
        if (updateDTO.getCategoryId() != null) {
            categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("分类不存在"));
            question.setCategoryId(updateDTO.getCategoryId());
        }
        
        if (updateDTO.getTitle() != null) {
            question.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getContent() != null) {
            question.setContent(updateDTO.getContent());
        }
        if (updateDTO.getQuestionType() != null) {
            question.setQuestionType(updateDTO.getQuestionType());
        }
        if (updateDTO.getDifficulty() != null) {
            question.setDifficulty(updateDTO.getDifficulty());
        }
        if (updateDTO.getTags() != null) {
            question.setTags(updateDTO.getTags());
        }
        if (updateDTO.getKeyPoints() != null) {
            question.setKeyPoints(updateDTO.getKeyPoints());
        }
        if (updateDTO.getOptions() != null) {
            question.setOptions(updateDTO.getOptions());
        }
        if (updateDTO.getCorrectAnswer() != null) {
            question.setCorrectAnswer(updateDTO.getCorrectAnswer());
        }
        if (updateDTO.getAnswerExplanation() != null) {
            question.setAnswerExplanation(updateDTO.getAnswerExplanation());
        }
        if (updateDTO.getCodeTemplate() != null) {
            question.setCodeTemplate(updateDTO.getCodeTemplate());
        }
        if (updateDTO.getTestCases() != null) {
            question.setTestCases(updateDTO.getTestCases());
        }
        if (updateDTO.getLanguage() != null) {
            question.setLanguage(updateDTO.getLanguage());
        }
        
        questionRepository.save(question);
    }
    
    /**
     * 删除题目
     */
    @Transactional
    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new DataNotFoundException("题目不存在");
        }
        questionRepository.deleteById(questionId);
    }
    
    /**
     * 发布题目
     */
    @Transactional
    public void publishQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new DataNotFoundException("题目不存在"));
        
        question.setStatus(2); // 已发布
        question.setPublishedAt(java.time.LocalDateTime.now());
        questionRepository.save(question);
    }
    
    /**
     * 获取相似题目
     */
    @Transactional(readOnly = true)
    public List<QuestionSimilarDTO> getSimilarQuestions(Long questionId, int count) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new DataNotFoundException("题目不存在"));
        
        // 这里实现简单的相似度算法
        // 实际项目中应该使用更复杂的算法，如基于内容、标签、分类的相似度计算
        List<Question> similarQuestions = questionRepository.findSimilarQuestions(
                question.getCategoryId(), 
                question.getDifficulty(), 
                questionId, 
                count);
        
        return similarQuestions.stream()
                .map(this::convertToQuestionSimilarDTO)
                .toList();
    }
    
    /**
     * 转换题目为列表DTO
     */
    private QuestionListDTO convertToQuestionListDTO(Question question) {
        QuestionListDTO dto = new QuestionListDTO();
        dto.setQuestionId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setQuestionType(question.getQuestionType());
        dto.setDifficulty(question.getDifficulty());
        dto.setCategoryId(question.getCategoryId());
        dto.setTags(question.getTags());
        dto.setViewCount(question.getViewCount());
        dto.setAnswerCount(question.getAnswerCount());
        dto.setCorrectRate(question.getAnswerCount() > 0 ? 
                (double) question.getCorrectCount() / question.getAnswerCount() * 100 : 0);
        dto.setFavoriteCount(question.getFavoriteCount());
        dto.setHotScore(question.getHotScore());
        dto.setCreatedAt(question.getCreatedAt());
        return dto;
    }
    
    /**
     * 转换题目为详情DTO
     */
    private QuestionDetailDTO convertToQuestionDetailDTO(Question question) {
        QuestionDetailDTO dto = new QuestionDetailDTO();
        dto.setQuestionId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setContent(question.getContent());
        dto.setQuestionType(question.getQuestionType());
        dto.setDifficulty(question.getDifficulty());
        dto.setCategoryId(question.getCategoryId());
        dto.setTags(question.getTags());
        dto.setKeyPoints(question.getKeyPoints());
        dto.setOptions(question.getOptions());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setAnswerExplanation(question.getAnswerExplanation());
        dto.setCodeTemplate(question.getCodeTemplate());
        dto.setTestCases(question.getTestCases());
        dto.setLanguage(question.getLanguage());
        dto.setSourceInfo(question.getSourceInfo());
        dto.setViewCount(question.getViewCount());
        dto.setAnswerCount(question.getAnswerCount());
        dto.setCorrectCount(question.getCorrectCount());
        dto.setFavoriteCount(question.getFavoriteCount());
        dto.setAvgTimeSpent(question.getAvgTimeSpent());
        dto.setCreatedAt(question.getCreatedAt());
        return dto;
    }
    
    /**
     * 转换题目为相似题目DTO
     */
    private QuestionSimilarDTO convertToQuestionSimilarDTO(Question question) {
        QuestionSimilarDTO dto = new QuestionSimilarDTO();
        dto.setQuestionId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setDifficulty(question.getDifficulty());
        dto.setAnswerCount(question.getAnswerCount());
        dto.setCorrectCount(question.getCorrectCount());
        return dto;
    }
}