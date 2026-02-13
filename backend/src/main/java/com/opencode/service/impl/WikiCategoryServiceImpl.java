package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.WikiCategory;
import com.opencode.mapper.WikiCategoryMapper;
import com.opencode.service.WikiCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiCategoryServiceImpl extends ServiceImpl<WikiCategoryMapper, WikiCategory> implements WikiCategoryService {
    
    @Override
    public List<WikiCategory> getAllCategories() {
        return this.list();
    }
    
    @Override
    public WikiCategory getCategoryById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveCategory(WikiCategory category) {
        return this.save(category);
    }
    
    @Override
    public boolean updateCategory(WikiCategory category) {
        return this.updateById(category);
    }
    
    @Override
    public boolean deleteCategory(Long id) {
        return this.removeById(id);
    }
}
