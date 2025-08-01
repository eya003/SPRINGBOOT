package com.example.documentclassifier.repository;

import com.example.documentclassifier.model.Document;
import com.example.documentclassifier.model.DocumentCategory;
import com.example.documentclassifier.model.ClassificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByUserId(String userId);
    
    List<Document> findByCategory(DocumentCategory category);
    
    List<Document> findByStatus(ClassificationStatus status);
    
    List<Document> findByUserIdAndCategory(String userId, DocumentCategory category);
    
    List<Document> findByUserIdAndStatus(String userId, ClassificationStatus status);
    
    Page<Document> findByUserId(String userId, Pageable pageable);
    
    @Query("SELECT d FROM Document d WHERE d.userId = :userId AND d.createdAt BETWEEN :startDate AND :endDate")
    List<Document> findByUserIdAndDateRange(@Param("userId") String userId, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.userId = :userId AND d.category = :category")
    Long countByUserIdAndCategory(@Param("userId") String userId, @Param("category") DocumentCategory category);
    
    @Query("SELECT d.category, COUNT(d) FROM Document d WHERE d.userId = :userId GROUP BY d.category")
    List<Object[]> getCategoryStatsByUserId(@Param("userId") String userId);
}