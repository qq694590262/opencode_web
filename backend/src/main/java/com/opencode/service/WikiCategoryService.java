package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.WikiCategory;

import java.util.List;

public interface WikiCategoryService extends IService<WikiCategory> {
    List<WikiCategory> getAllCategories();
    WikiCategory getCategoryById(Long id);
    boolean saveCategory(WikiCategory category);
    boolean updateCategory(WikiCategory category);
    boolean deleteCategory(Long id);
}
