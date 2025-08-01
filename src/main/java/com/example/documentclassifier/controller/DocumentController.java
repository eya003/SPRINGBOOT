package com.example.documentclassifier.controller;

import com.example.documentclassifier.dto.DocumentDto;
import com.example.documentclassifier.dto.DocumentUploadRequest;
import com.example.documentclassifier.dto.ClassificationResponse;
import com.example.documentclassifier.model.DocumentCategory;
import com.example.documentclassifier.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {
    
    private final DocumentService documentService;
    
    @PostMapping
    public ResponseEntity<DocumentDto> uploadDocument(@RequestBody DocumentUploadRequest request) {
        try {
            DocumentDto document = documentService.uploadDocument(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentDto>> getDocumentsByUser(@PathVariable String userId) {
        List<DocumentDto> documents = documentService.getDocumentsByUserId(userId);
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<Page<DocumentDto>> getDocumentsByUserPaginated(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DocumentDto> documents = documentService.getDocumentsByUserId(userId, pageable);
        
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<DocumentDto>> getDocumentsByCategory(@PathVariable DocumentCategory category) {
        List<DocumentDto> documents = documentService.getDocumentsByCategory(category);
        return ResponseEntity.ok(documents);
    }
    
    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<List<DocumentDto>> getDocumentsByUserAndCategory(
            @PathVariable String userId, 
            @PathVariable DocumentCategory category) {
        List<DocumentDto> documents = documentService.getDocumentsByUserAndCategory(userId, category);
        return ResponseEntity.ok(documents);
    }
    
    @PutMapping("/{id}/category")
    public ResponseEntity<DocumentDto> updateDocumentCategory(
            @PathVariable Long id, 
            @RequestBody DocumentCategory category) {
        try {
            DocumentDto updatedDocument = documentService.updateDocumentCategory(id, category);
            return ResponseEntity.ok(updatedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/classify")
    public ResponseEntity<ClassificationResponse> classifyDocument(@PathVariable Long id) {
        try {
            ClassificationResponse response = documentService.classifyDocument(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}