package com.claw.modules.wronganswer.repository;

import com.claw.modules.wronganswer.entity.WrongAnswerCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WrongAnswerCollectionRepository extends JpaRepository<WrongAnswerCollection, Long> {
    
    Page<WrongAnswerCollection> findByUserId(Long userId, Pageable pageable);
    
    Page<WrongAnswerCollection> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable);
    
    List<WrongAnswerCollection> findByUserIdAndIsPublic(Long userId, Boolean isPublic);
    
    @Query("SELECT c FROM WrongAnswerCollection c WHERE c.isPublic = true OR c.userId = :userId")
    Page<WrongAnswerCollection> findVisibleCollections(@Param("userId") Long userId, Pageable pageable);
    
    boolean existsByUserIdAndName(Long userId, String name);
    
    int countByUserId(Long userId);
}