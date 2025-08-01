package com.example.documentclassifier.dto;

import com.example.documentclassifier.model.DocumentCategory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResponse {
    private DocumentCategory predictedCategory;
    private Double confidence;
    private String explanation;
}