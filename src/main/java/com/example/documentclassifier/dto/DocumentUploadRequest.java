package com.example.documentclassifier.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadRequest {
    private String title;
    private String content;
    private String fileName;
    private String fileType;
    private String userId;
}