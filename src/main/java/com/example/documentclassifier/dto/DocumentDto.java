package com.example.documentclassifier.dto;

import com.example.documentclassifier.model.DocumentCategory;
import com.example.documentclassifier.model.ClassificationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private Long id;
    private String title;
    private String content;
    private String fileName;
    private String fileType;
    private DocumentCategory category;
    private ClassificationStatus status;
    private Double confidenceScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;
}