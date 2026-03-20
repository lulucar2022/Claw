package com.claw.modules.question.repository;

import com.claw.modules.question.entity.Question;
import com.claw.modules.question.enums.DifficultyLevel;
import com.claw.modules.question.enums.QuestionStatus;
import com.claw.modules.question.enums.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 题目数据访问层
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

    /**
     * 根据分类ID查找题目
     */
    List<Question> findByCategoryId(Long categoryId);

    /**
     * 根据分类ID和状态查找题目
     */
    List<Question> findByCategoryIdAndStatus(Long categoryId, QuestionStatus status);

    /**
     * 根据分类ID和难度查找题目
     */
    List<Question> findByCategoryIdAndDifficulty(Long categoryId, DifficultyLevel difficulty);

    /**
     * 根据分类ID、难度和状态查找题目
     */
    @Query("SELECT q FROM Question q WHERE q.categoryId = :categoryId AND q.difficulty = :difficulty AND q.status = :status")
    List<Question> findByCategoryIdAndDifficultyAndStatus(@Param("categoryId") Long categoryId,
                                                         @Param("difficulty") DifficultyLevel difficulty,
                                                         @Param("status") QuestionStatus status);

    /**
     * 根据标题模糊搜索
     */
    @Query("SELECT q FROM Question q WHERE q.deletedAt IS NULL AND q.title LIKE %:keyword%")
    Page<Question> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据内容模糊搜索
     */
    @Query("SELECT q FROM Question q WHERE q.deletedAt IS NULL AND q.content LIKE %:keyword%")
    Page<Question> searchByContent(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据标题或内容模糊搜索
     */
    @Query("SELECT q FROM Question q WHERE q.deletedAt IS NULL AND (q.title LIKE %:keyword% OR q.content LIKE %:keyword%)")
    Page<Question> searchByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据难度查找题目
     */
    List<Question> findByDifficulty(DifficultyLevel difficulty);

    /**
     * 根据状态查找题目
     */
    List<Question> findByStatus(QuestionStatus status);

    /**
     * 根据创建人查找题目
     */
    List<Question> findByCreatedBy(Long createdBy);

    /**
     * 根据创建人和状态查找题目
     */
    @Query("SELECT q FROM Question q WHERE q.createdBy = :createdBy AND q.status = :status")
    List<Question> findByCreatedByAndStatus(@Param("createdBy") Long createdBy,
                                           @Param("status") QuestionStatus status);

    /**
     * 根据题目类型查找
     */
    List<Question> findByQuestionType(QuestionType questionType);

    /**
     * 更新浏览次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.viewCount = q.viewCount + 1 WHERE q.id = :questionId")
    int incrementViewCount(@Param("questionId") Long questionId);

    /**
     * 更新答题次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.answerCount = q.answerCount + 1 WHERE q.id = :questionId")
    int incrementAnswerCount(@Param("questionId") Long questionId);

    /**
     * 更新正确次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.correctCount = q.correctCount + 1 WHERE q.id = :questionId")
    int incrementCorrectCount(@Param("questionId") Long questionId);

    /**
     * 更新错误次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.wrongCount = q.wrongCount + 1 WHERE q.id = :questionId")
    int incrementWrongCount(@Param("questionId") Long questionId);

    /**
     * 更新收藏次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.favoriteCount = q.favoriteCount + 1 WHERE q.id = :questionId")
    int incrementFavoriteCount(@Param("questionId") Long questionId);

    /**
     * 减少收藏次数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.favoriteCount = q.favoriteCount - 1 WHERE q.id = :questionId AND q.favoriteCount > 0")
    int decrementFavoriteCount(@Param("questionId") Long questionId);

    /**
     * 更新题目状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.status = :status WHERE q.id = :questionId")
    int updateStatus(@Param("questionId") Long questionId, 
                    @Param("status") QuestionStatus status);

    /**
     * 批量更新题目状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.status = :status WHERE q.id IN :questionIds")
    int batchUpdateStatus(@Param("questionIds") List<Long> questionIds, 
                         @Param("status") QuestionStatus status);

    /**
     * 更新审核信息
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.reviewerId = :reviewerId, q.reviewedAt = :reviewedAt, q.reviewComment = :reviewComment WHERE q.id = :questionId")
    int updateReviewInfo(@Param("questionId") Long questionId,
                        @Param("reviewerId") Long reviewerId,
                        @Param("reviewedAt") LocalDateTime reviewedAt,
                        @Param("reviewComment") String reviewComment);

    /**
     * 统计活跃题目数量（已发布）
     */
    @Query("SELECT COUNT(q) FROM Question q WHERE q.status = :status AND q.deletedAt IS NULL")
    long countByStatus(@Param("status") QuestionStatus status);

    /**
     * 根据分类ID统计题目数量
     */
    @Query("SELECT COUNT(q) FROM Question q WHERE q.categoryId = :categoryId AND q.deletedAt IS NULL")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据分类ID和状态统计题目数量
     */
    @Query("SELECT COUNT(q) FROM Question q WHERE q.categoryId = :categoryId AND q.status = :status AND q.deletedAt IS NULL")
    long countByCategoryIdAndStatus(@Param("categoryId") Long categoryId,
                                   @Param("status") QuestionStatus status);

    /**
     * 根据标签查找题目
     */
    @Query(value = "SELECT * FROM questions q WHERE JSON_CONTAINS(q.tags, :tag) AND q.deleted_at IS NULL", 
           nativeQuery = true)
    List<Question> findByTag(@Param("tag") String tag);

    /**
     * 根据关键词、分类和难度综合搜索
     */
    @Query("SELECT q FROM Question q WHERE q.deletedAt IS NULL AND " +
           "(:keyword IS NULL OR q.title LIKE %:keyword% OR q.content LIKE %:keyword%) AND " +
           "(:categoryId IS NULL OR q.categoryId = :categoryId) AND " +
           "(:difficulty IS NULL OR q.difficulty = :difficulty) AND " +
           "(:status IS NULL OR q.status = :status)")
    Page<Question> search(@Param("keyword") String keyword,
                         @Param("categoryId") Long categoryId,
                         @Param("difficulty") DifficultyLevel difficulty,
                         @Param("status") QuestionStatus status,
                         Pageable pageable);

    /**
     * 查找热点题目（按热度分数排序）
     */
    @Query("SELECT q FROM Question q WHERE q.status = :status AND q.deletedAt IS NULL ORDER BY q.hotScore DESC")
    Page<Question> findHotQuestions(@Param("status") QuestionStatus status, Pageable pageable);

    /**
     * 查找推荐题目
     */
    @Query("SELECT q FROM Question q WHERE q.status = :status AND q.isRecommended = true AND q.deletedAt IS NULL ORDER BY q.hotScore DESC")
    Page<Question> findRecommendedQuestions(@Param("status") QuestionStatus status, Pageable pageable);

    /**
     * 查找最新发布的题目
     */
    @Query("SELECT q FROM Question q WHERE q.status = :status AND q.deletedAt IS NULL ORDER BY q.publishedAt DESC")
    Page<Question> findLatestQuestions(@Param("status") QuestionStatus status, Pageable pageable);

    /**
     * 查找随机题目（用于推荐）
     */
    @Query(value = "SELECT * FROM questions q WHERE q.status = :status AND q.deleted_at IS NULL ORDER BY RAND() LIMIT :limit",
           nativeQuery = true)
    List<Question> findRandomQuestions(@Param("status") QuestionStatus status,
                                      @Param("limit") int limit);

    /**
     * 更新热度分数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.hotScore = (:viewCount * 0.3 + :answerCount * 0.4 + :favoriteCount * 0.3) WHERE q.id = :questionId")
    int updateHotScore(@Param("questionId") Long questionId,
                      @Param("viewCount") Integer viewCount,
                      @Param("answerCount") Integer answerCount,
                      @Param("favoriteCount") Integer favoriteCount);

    /**
     * 根据正确率范围查找题目
     */
    @Query("SELECT q FROM Question q WHERE q.deletedAt IS NULL AND " +
           "(q.correctCount * 100.0 / q.answerCount) BETWEEN :minRate AND :maxRate")
    List<Question> findByCorrectRateRange(@Param("minRate") Double minRate,
                                         @Param("maxRate") Double maxRate);

    /**
     * 根据分类ID列表查找题目
     */
    @Query("SELECT q FROM Question q WHERE q.categoryId IN :categoryIds AND q.deletedAt IS NULL")
    List<Question> findByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);

    /**
     * 根据ID列表和状态查找题目
     */
    @Query("SELECT q FROM Question q WHERE q.id IN :questionIds AND q.status = :status AND q.deletedAt IS NULL")
    List<Question> findByIdInAndStatus(@Param("questionIds") List<Long> questionIds,
                                      @Param("status") QuestionStatus status);

    /**
     * 统计用户创建的题目数量
     */
    @Query("SELECT COUNT(q) FROM Question q WHERE q.createdBy = :userId AND q.deletedAt IS NULL")
    long countByCreatedBy(@Param("userId") Long userId);

    /**
     * 根据发布时间范围查找题目
     */
    @Query("SELECT q FROM Question q WHERE q.publishedAt BETWEEN :startDate AND :endDate AND q.deletedAt IS NULL")
    List<Question> findByPublishedAtBetween(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * 查找相似题目
     */
    @Query("SELECT q FROM Question q WHERE q.categoryId = :categoryId AND q.difficulty = :difficulty AND q.id != :excludeId AND q.status = :status AND q.deletedAt IS NULL ORDER BY q.hotScore DESC LIMIT :limit")
    List<Question> findSimilarQuestions(@Param("categoryId") Long categoryId,
                                      @Param("difficulty") DifficultyLevel difficulty,
                                      @Param("excludeId") Long excludeId,
                                      @Param("limit") int limit,
                                      @Param("status") QuestionStatus status);
}