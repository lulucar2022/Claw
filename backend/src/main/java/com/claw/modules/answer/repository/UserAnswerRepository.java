package com.claw.modules.answer.repository;

import com.claw.modules.answer.entity.UserAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    
    Page<UserAnswer> findByUserId(Long userId, Pageable pageable);
    
    Page<UserAnswer> findByUserIdAndQuestionId(Long userId, Long questionId, Pageable pageable);
    
    Page<UserAnswer> findByUserIdAndIsCorrect(Long userId, Boolean isCorrect, Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM UserAnswer a WHERE a.userId = :userId AND a.isCorrect = true")
    long countCorrectAnswersByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(a) FROM UserAnswer a WHERE a.userId = :userId AND a.isCorrect = false")
    long countWrongAnswersByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(a.timeSpent) FROM UserAnswer a WHERE a.userId = :userId")
    Long sumTimeSpentByUserId(@Param("userId") Long userId);
    
    @Query("SELECT a FROM UserAnswer a WHERE a.userId = :userId AND a.nextReviewAt <= :now AND a.isReviewed = false")
    List<UserAnswer> findAnswersNeedReview(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    @Query("SELECT q.categoryId, COUNT(a), SUM(CASE WHEN a.isCorrect = true THEN 1 ELSE 0 END) " +
           "FROM UserAnswer a JOIN Question q ON a.questionId = q.id " +
           "WHERE a.userId = :userId GROUP BY q.categoryId")
    List<Object[]> findCategoryStatsByUserId(@Param("userId") Long userId);
    
    boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
    
    List<UserAnswer> findByUserIdAndQuestionIdOrderByAnsweredAtDesc(Long userId, Long questionId);
}