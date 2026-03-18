package com.claw.modules.wronganswer.repository;

import com.claw.modules.wronganswer.entity.WrongAnswerItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WrongAnswerItemRepository extends JpaRepository<WrongAnswerItem, Long> {
    
    Page<WrongAnswerItem> findByCollectionId(Long collectionId, Pageable pageable);
    
    Optional<WrongAnswerItem> findByCollectionIdAndQuestionId(Long collectionId, Long questionId);
    
    List<WrongAnswerItem> findByQuestionId(Long questionId);
    
    int countByCollectionId(Long collectionId);
    
    @Query("SELECT i FROM WrongAnswerItem i WHERE i.collectionId = :collectionId AND i.nextReviewAt <= :now AND i.isMastered = false")
    List<WrongAnswerItem> findItemsNeedReview(@Param("collectionId") Long collectionId, @Param("now") LocalDateTime now);
    
    @Query("SELECT i.questionId, COUNT(i), SUM(CASE WHEN i.isMastered = true THEN 1 ELSE 0 END) " +
           "FROM WrongAnswerItem i WHERE i.collectionId = :collectionId GROUP BY i.questionId")
    List<Object[]> findQuestionStatsByCollection(@Param("collectionId") Long collectionId);
    
    @Query("SELECT i FROM WrongAnswerItem i WHERE i.collectionId IN :collectionIds AND i.isMastered = false AND i.nextReviewAt <= :now")
    List<WrongAnswerItem> findDueItemsByCollections(@Param("collectionIds") List<Long> collectionIds, @Param("now") LocalDateTime now);
    
    boolean existsByCollectionIdAndQuestionId(Long collectionId, Long questionId);
}