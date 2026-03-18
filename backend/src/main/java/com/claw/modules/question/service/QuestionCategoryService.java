package com.claw.modules.question.service;

import com.claw.modules.question.entity.QuestionCategory;
import com.claw.modules.question.repository.QuestionCategoryRepository;
import com.claw.common.exception.DataNotFoundException;
import com.claw.modules.question.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionCategoryService {
    
    private final QuestionCategoryRepository categoryRepository;
    
    public QuestionCategoryService(QuestionCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * 获取分类树
     */
    @Transactional(readOnly = true)
    public List<CategoryTreeDTO> getCategoryTree(Long parentId, Boolean includeStatistics) {
        List<QuestionCategory> categories;
        
        if (parentId == null || parentId == 0) {
            categories = categoryRepository.findByLevel(1);
        } else {
            categories = categoryRepository.findByParentId(parentId);
        }
        
        return categories.stream()
                .map(category -> convertToCategoryTreeDTO(category, includeStatistics))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取分类详情
     */
    @Transactional(readOnly = true)
    public CategoryDetailDTO getCategoryDetail(Long categoryId) {
        QuestionCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("分类不存在"));
        
        return convertToCategoryDetailDTO(category);
    }
    
    /**
     * 创建分类
     */
    @Transactional
    public Long createCategory(CategoryCreateDTO createDTO) {
        QuestionCategory category = new QuestionCategory();
        category.setName(createDTO.getName());
        category.setSlug(createDTO.getSlug());
        category.setDescription(createDTO.getDescription());
        category.setIconUrl(createDTO.getIconUrl());
        category.setParentId(createDTO.getParentId());
        category.setLevel(createDTO.getParentId() == 0 ? 1 : 2);
        category.setSortOrder(createDTO.getSortOrder());
        category.setIsActive(true);
        
        QuestionCategory savedCategory = categoryRepository.save(category);
        return savedCategory.getId();
    }
    
    /**
     * 更新分类
     */
    @Transactional
    public void updateCategory(Long categoryId, CategoryUpdateDTO updateDTO) {
        QuestionCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("分类不存在"));
        
        if (updateDTO.getName() != null) {
            category.setName(updateDTO.getName());
        }
        if (updateDTO.getSlug() != null) {
            category.setSlug(updateDTO.getSlug());
        }
        if (updateDTO.getDescription() != null) {
            category.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getIconUrl() != null) {
            category.setIconUrl(updateDTO.getIconUrl());
        }
        if (updateDTO.getSortOrder() != null) {
            category.setSortOrder(updateDTO.getSortOrder());
        }
        if (updateDTO.getIsActive() != null) {
            category.setIsActive(updateDTO.getIsActive());
        }
        
        categoryRepository.save(category);
    }
    
    /**
     * 删除分类
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new DataNotFoundException("分类不存在");
        }
        categoryRepository.deleteById(categoryId);
    }
    
    /**
     * 转换分类为树形DTO
     */
    private CategoryTreeDTO convertToCategoryTreeDTO(QuestionCategory category, Boolean includeStatistics) {
        CategoryTreeDTO dto = new CategoryTreeDTO();
        dto.setCategoryId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        dto.setLevel(category.getLevel());
        dto.setSortOrder(category.getSortOrder());
        
        if (includeStatistics != null && includeStatistics) {
            dto.setQuestionCount(category.getQuestionCount());
            dto.setStudyCount(category.getStudyCount());
        }
        
        // 递归获取子分类
        if (category.getLevel() < 3) { // 假设最多3级分类
            List<QuestionCategory> children = categoryRepository.findByParentId(category.getId());
            List<CategoryTreeDTO> childrenDTOs = children.stream()
                    .map(child -> convertToCategoryTreeDTO(child, includeStatistics))
                    .collect(Collectors.toList());
            dto.setChildren(childrenDTOs);
        }
        
        return dto;
    }
    
    /**
     * 转换分类为详情DTO
     */
    private CategoryDetailDTO convertToCategoryDetailDTO(QuestionCategory category) {
        CategoryDetailDTO dto = new CategoryDetailDTO();
        dto.setCategoryId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        dto.setLevel(category.getLevel());
        dto.setParentId(category.getParentId());
        dto.setSortOrder(category.getSortOrder());
        dto.setQuestionCount(category.getQuestionCount());
        dto.setStudyCount(category.getStudyCount());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
}