package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Document;

import java.util.List;

public interface DocumentService extends IService<Document> {
    List<Document> getAllDocuments();
    Document getDocumentById(Long id);
    boolean saveDocument(Document document);
    boolean updateDocument(Document document);
    boolean deleteDocument(Long id);
}
