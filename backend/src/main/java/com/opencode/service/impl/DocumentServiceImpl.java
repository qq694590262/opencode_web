package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Document;
import com.opencode.mapper.DocumentMapper;
import com.opencode.service.DocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {
    
    @Override
    public List<Document> getAllDocuments() {
        return this.list();
    }
    
    @Override
    public Document getDocumentById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveDocument(Document document) {
        return this.save(document);
    }
    
    @Override
    public boolean updateDocument(Document document) {
        return this.updateById(document);
    }
    
    @Override
    public boolean deleteDocument(Long id) {
        return this.removeById(id);
    }
}
