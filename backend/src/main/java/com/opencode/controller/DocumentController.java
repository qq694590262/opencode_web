package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Document;
import com.opencode.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    
    private final DocumentService documentService;
    
    @GetMapping
    public Result<List<Document>> getAllDocuments() {
        return Result.success(documentService.getAllDocuments());
    }
    
    @GetMapping("/{id}")
    public Result<Document> getDocumentById(@PathVariable Long id) {
        return Result.success(documentService.getDocumentById(id));
    }
    
    @PostMapping
    public Result<Boolean> createDocument(@RequestBody Document document) {
        return Result.success(documentService.saveDocument(document));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        document.setId(id);
        return Result.success(documentService.updateDocument(document));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDocument(@PathVariable Long id) {
        return Result.success(documentService.deleteDocument(id));
    }
}
