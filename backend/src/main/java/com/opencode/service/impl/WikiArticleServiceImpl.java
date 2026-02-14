package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.WikiArticle;
import com.opencode.mapper.WikiArticleMapper;
import com.opencode.service.WikiArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiArticleServiceImpl extends ServiceImpl<WikiArticleMapper, WikiArticle> implements WikiArticleService {
    
    @Override
    public List<WikiArticle> getArticlesByCategory(Long categoryId) {
        LambdaQueryWrapper<WikiArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WikiArticle::getCategoryId, categoryId)
               .eq(WikiArticle::getDeleted, 0)
               .orderByDesc(WikiArticle::getCreateTime);
        return list(wrapper);
    }
    
    @Override
    public boolean incrementViewCount(Long id) {
        WikiArticle article = getById(id);
        if (article != null) {
            article.setViewCount((article.getViewCount() == null ? 0 : article.getViewCount()) + 1);
            return updateById(article);
        }
        return false;
    }
    
    @Override
    public boolean incrementLikeCount(Long id) {
        WikiArticle article = getById(id);
        if (article != null) {
            article.setLikeCount((article.getLikeCount() == null ? 0 : article.getLikeCount()) + 1);
            return updateById(article);
        }
        return false;
    }
}
