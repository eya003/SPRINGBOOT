package com.example.documentclassifier.service;

import com.example.documentclassifier.dto.ClassificationResponse;
import com.example.documentclassifier.model.DocumentCategory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ClassificationService {
    
    private final Random random = new Random();
    
    public ClassificationResponse classifyDocument(String content) {
        // This is a mock implementation
        // In a real application, you would integrate with ML models or external APIs
        
        DocumentCategory predictedCategory = classifyContent(content);
        Double confidence = 0.7 + (random.nextDouble() * 0.3); // Random confidence between 0.7 and 1.0
        String explanation = generateExplanation(predictedCategory, content);
        
        return new ClassificationResponse(predictedCategory, confidence, explanation);
    }
    
    private DocumentCategory classifyContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return DocumentCategory.OTHER;
        }
        
        String lowerContent = content.toLowerCase();
        
        // Simple keyword-based classification
        if (lowerContent.contains("invoice") || lowerContent.contains("bill") || lowerContent.contains("payment")) {
            return DocumentCategory.INVOICE;
        } else if (lowerContent.contains("contract") || lowerContent.contains("agreement") || lowerContent.contains("terms")) {
            return DocumentCategory.CONTRACT;
        } else if (lowerContent.contains("report") || lowerContent.contains("analysis") || lowerContent.contains("summary")) {
            return DocumentCategory.REPORT;
        } else if (lowerContent.contains("legal") || lowerContent.contains("law") || lowerContent.contains("court")) {
            return DocumentCategory.LEGAL;
        } else if (lowerContent.contains("financial") || lowerContent.contains("budget") || lowerContent.contains("revenue")) {
            return DocumentCategory.FINANCIAL;
        } else if (lowerContent.contains("technical") || lowerContent.contains("specification") || lowerContent.contains("manual")) {
            return DocumentCategory.TECHNICAL;
        } else if (lowerContent.contains("marketing") || lowerContent.contains("campaign") || lowerContent.contains("promotion")) {
            return DocumentCategory.MARKETING;
        } else if (lowerContent.contains("hr") || lowerContent.contains("employee") || lowerContent.contains("hiring")) {
            return DocumentCategory.HR;
        } else if (lowerContent.contains("dear") || lowerContent.contains("sincerely") || lowerContent.contains("regards")) {
            return DocumentCategory.LETTER;
        } else {
            return DocumentCategory.OTHER;
        }
    }
    
    private String generateExplanation(DocumentCategory category, String content) {
        return String.format("Document classified as %s based on content analysis. " +
                "Key indicators found in the document content.", category.name());
    }
}