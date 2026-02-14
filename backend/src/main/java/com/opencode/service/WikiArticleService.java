package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.WikiArticle;

import java.util.List;

public interface WikiArticleService extends IService<WikiArticle> {
    
    List<WikiArticle> getArticlesByCategory(Long categoryId);
    
    boolean incrementViewCount(Long id);
    
    boolean incrementLikeCount(Long id);
}
