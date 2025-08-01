package com.example.documentclassifier.service;

import com.example.documentclassifier.dto.DocumentDto;
import com.example.documentclassifier.dto.DocumentUploadRequest;
import com.example.documentclassifier.dto.ClassificationResponse;
import com.example.documentclassifier.model.Document;
import com.example.documentclassifier.model.DocumentCategory;
import com.example.documentclassifier.model.ClassificationStatus;
import com.example.documentclassifier.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final ClassificationService classificationService;
    
    public DocumentDto uploadDocument(DocumentUploadRequest request) {
        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setContent(request.getContent());
        document.setFileName(request.getFileName());
        document.setFileType(request.getFileType());
        document.setUserId(request.getUserId());
        document.setStatus(ClassificationStatus.PENDING);
        
        Document savedDocument = documentRepository.save(document);
        
        // Trigger classification asynchronously
        classifyDocumentAsync(savedDocument.getId());
        
        return convertToDto(savedDocument);
    }
    
    public Optional<DocumentDto> getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<DocumentDto> getDocumentsByUserId(String userId) {
        return documentRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Page<DocumentDto> getDocumentsByUserId(String userId, Pageable pageable) {
        return documentRepository.findByUserId(userId, pageable)
                .map(this::convertToDto);
    }
    
    public List<DocumentDto> getDocumentsByCategory(DocumentCategory category) {
        return documentRepository.findByCategory(category)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<DocumentDto> getDocumentsByUserAndCategory(String userId, DocumentCategory category) {
        return documentRepository.findByUserIdAndCategory(userId, category)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public DocumentDto updateDocumentCategory(Long id, DocumentCategory category) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        document.setCategory(category);
        document.setStatus(ClassificationStatus.COMPLETED);
        
        Document updatedDocument = documentRepository.save(document);
        return convertToDto(updatedDocument);
    }
    
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
    
    public ClassificationResponse classifyDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        ClassificationResponse response = classificationService.classifyDocument(document.getContent());
        
        document.setCategory(response.getPredictedCategory());
        document.setConfidenceScore(response.getConfidence());
        document.setStatus(ClassificationStatus.COMPLETED);
        
        documentRepository.save(document);
        
        return response;
    }
    
    private void classifyDocumentAsync(Long documentId) {
        // In a real application, this would be done asynchronously
        try {
            classifyDocument(documentId);
        } catch (Exception e) {
            // Handle classification failure
            Document document = documentRepository.findById(documentId).orElse(null);
            if (document != null) {
                document.setStatus(ClassificationStatus.FAILED);
                documentRepository.save(document);
            }
        }
    }
    
    private DocumentDto convertToDto(Document document) {
        return new DocumentDto(
                document.getId(),
                document.getTitle(),
                document.getContent(),
                document.getFileName(),
                document.getFileType(),
                document.getCategory(),
                document.getStatus(),
                document.getConfidenceScore(),
                document.getCreatedAt(),
                document.getUpdatedAt(),
                document.getUserId()
        );
    }
}