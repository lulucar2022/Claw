package com.claw.modules.question.repository;

import com.claw.modules.question.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 题目分类数据访问层
 */
@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {

    /**
     * 根据父分类ID查找分类
     */
    List<QuestionCategory> findByParentId(Long parentId);

    /**
     * 根据父分类ID和激活状态查找分类
     */
    List<QuestionCategory> findByParentIdAndIsActive(Long parentId, Boolean isActive);

    /**
     * 根据slug查找分类
     */
    Optional<QuestionCategory> findBySlug(String slug);

    /**
     * 根据层级查找分类
     */
    List<QuestionCategory> findByLevel(Integer level);

    /**
     * 根据激活状态查找分类
     */
    List<QuestionCategory> findByIsActive(Boolean isActive);

    /**
     * 根据父分类ID和层级查找分类
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.parentId = :parentId AND qc.level = :level")
    List<QuestionCategory> findByParentIdAndLevel(@Param("parentId") Long parentId, 
                                                 @Param("level") Integer level);

    /**
     * 根据父分类ID和激活状态查找分类，按排序字段排序
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.parentId = :parentId AND qc.isActive = :isActive ORDER BY qc.sortOrder ASC")
    List<QuestionCategory> findByParentIdAndIsActiveOrderBySortOrder(@Param("parentId") Long parentId, 
                                                                     @Param("isActive") Boolean isActive);

    /**
     * 根据名称模糊搜索分类
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.name LIKE %:keyword% AND qc.deletedAt IS NULL")
    Page<QuestionCategory> searchByName(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 查找根分类（parentId为0或null）
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE (qc.parentId IS NULL OR qc.parentId = 0) AND qc.deletedAt IS NULL")
    List<QuestionCategory> findRootCategories();

    /**
     * 根据激活状态查找根分类
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE (qc.parentId IS NULL OR qc.parentId = 0) AND qc.isActive = :isActive AND qc.deletedAt IS NULL")
    List<QuestionCategory> findRootCategoriesByIsActive(@Param("isActive") Boolean isActive);

    /**
     * 统计分类下的题目数量
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.deletedAt IS NULL ORDER BY qc.questionCount DESC")
    Page<QuestionCategory> findCategoriesByQuestionCount(Pageable pageable);

    /**
     * 更新分类的题目数量
     */
    @Modifying
    @Transactional
    @Query("UPDATE QuestionCategory qc SET qc.questionCount = qc.questionCount + :increment WHERE qc.id = :categoryId")
    int updateQuestionCount(@Param("categoryId") Long categoryId, 
                           @Param("increment") Integer increment);

    /**
     * 更新分类的学习次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE QuestionCategory qc SET qc.studyCount = qc.studyCount + :increment WHERE qc.id = :categoryId")
    int updateStudyCount(@Param("categoryId") Long categoryId, 
                        @Param("increment") Integer increment);

    /**
     * 更新分类的激活状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE QuestionCategory qc SET qc.isActive = :isActive WHERE qc.id = :categoryId")
    int updateIsActive(@Param("categoryId") Long categoryId, 
                      @Param("isActive") Boolean isActive);

    /**
     * 批量更新分类激活状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE QuestionCategory qc SET qc.isActive = :isActive WHERE qc.id IN :categoryIds")
    int batchUpdateIsActive(@Param("categoryIds") List<Long> categoryIds, 
                            @Param("isActive") Boolean isActive);

    /**
     * 根据父分类ID统计子分类数量
     */
    @Query("SELECT COUNT(qc) FROM QuestionCategory qc WHERE qc.parentId = :parentId AND qc.deletedAt IS NULL")
    long countByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父分类ID和激活状态统计子分类数量
     */
    @Query("SELECT COUNT(qc) FROM QuestionCategory qc WHERE qc.parentId = :parentId AND qc.isActive = :isActive AND qc.deletedAt IS NULL")
    long countByParentIdAndIsActive(@Param("parentId") Long parentId, 
                                   @Param("isActive") Boolean isActive);

    /**
     * 获取分类树结构
     */
    @Query(value = "WITH RECURSIVE category_tree AS (" +
                   "SELECT id, parent_id, name, slug, level, sort_order, is_active, question_count " +
                   "FROM question_categories " +
                   "WHERE parent_id = 0 AND deleted_at IS NULL " +
                   "UNION ALL " +
                   "SELECT c.id, c.parent_id, c.name, c.slug, c.level, c.sort_order, c.is_active, c.question_count " +
                   "FROM question_categories c " +
                   "INNER JOIN category_tree ct ON c.parent_id = ct.id " +
                   "WHERE c.deleted_at IS NULL" +
                   ") " +
                   "SELECT * FROM category_tree ORDER BY sort_order",
           nativeQuery = true)
    List<Object[]> findCategoryTree();

    /**
     * 查找叶子分类
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.isLeaf = true AND qc.isActive = true AND qc.deletedAt IS NULL")
    List<QuestionCategory> findLeafCategories();

    /**
     * 根据分类ID列表查找
     */
    List<QuestionCategory> findByIdIn(List<Long> ids);

    /**
     * 根据分类ID列表和激活状态查找
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.id IN :ids AND qc.isActive = :isActive AND qc.deletedAt IS NULL")
    List<QuestionCategory> findByIdInAndIsActive(@Param("ids") List<Long> ids, 
                                                @Param("isActive") Boolean isActive);

    /**
     * 根据名称查找
     */
    Optional<QuestionCategory> findByName(String name);

    /**
     * 根据名称模糊查找
     */
    @Query("SELECT qc FROM QuestionCategory qc WHERE qc.name LIKE %:name% AND qc.deletedAt IS NULL")
    List<QuestionCategory> findByNameContaining(@Param("name") String name);

    /**
     * 根据分类ID和激活状态统计题目数量
     */
    @Query("SELECT qc.questionCount FROM QuestionCategory qc WHERE qc.id = :categoryId AND qc.isActive = true")
    Optional<Integer> findQuestionCountByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据父分类ID统计题目总数
     */
    @Query("SELECT SUM(qc.questionCount) FROM QuestionCategory qc WHERE qc.parentId = :parentId AND qc.isActive = true AND qc.deletedAt IS NULL")
    Long sumQuestionCountByParentId(@Param("parentId") Long parentId);
}