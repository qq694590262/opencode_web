package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.WikiArticle;
import com.opencode.entity.WikiCategory;
import com.opencode.service.WikiArticleService;
import com.opencode.service.WikiCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wiki")
@RequiredArgsConstructor
public class WikiController {
    
    private final WikiCategoryService wikiCategoryService;
    private final WikiArticleService wikiArticleService;
    
    @GetMapping("/categories")
    public Result<List<WikiCategory>> getAllCategories() {
        return Result.success(wikiCategoryService.getAllCategories());
    }
    
    @GetMapping("/categories/{id}")
    public Result<WikiCategory> getCategoryById(@PathVariable Long id) {
        return Result.success(wikiCategoryService.getCategoryById(id));
    }
    
    @PostMapping("/categories")
    public Result<Boolean> createCategory(@RequestBody WikiCategory category) {
        return Result.success(wikiCategoryService.saveCategory(category));
    }
    
    @PutMapping("/categories/{id}")
    public Result<Boolean> updateCategory(@PathVariable Long id, @RequestBody WikiCategory category) {
        category.setId(id);
        return Result.success(wikiCategoryService.updateCategory(category));
    }
    
    @DeleteMapping("/categories/{id}")
    public Result<Boolean> deleteCategory(@PathVariable Long id) {
        return Result.success(wikiCategoryService.deleteCategory(id));
    }
    
    // 文章相关API
    @GetMapping("/articles")
    public Result<List<WikiArticle>> getAllArticles() {
        return Result.success(wikiArticleService.list());
    }
    
    @GetMapping("/articles/{id}")
    public Result<WikiArticle> getArticleById(@PathVariable Long id) {
        return Result.success(wikiArticleService.getById(id));
    }
    
    @GetMapping("/categories/{categoryId}/articles")
    public Result<List<WikiArticle>> getArticlesByCategory(@PathVariable Long categoryId) {
        return Result.success(wikiArticleService.getArticlesByCategory(categoryId));
    }
    
    @PostMapping("/articles")
    public Result<Boolean> createArticle(@RequestBody WikiArticle article) {
        return Result.success(wikiArticleService.save(article));
    }
    
    @PutMapping("/articles/{id}")
    public Result<Boolean> updateArticle(@PathVariable Long id, @RequestBody WikiArticle article) {
        article.setId(id);
        return Result.success(wikiArticleService.updateById(article));
    }
    
    @DeleteMapping("/articles/{id}")
    public Result<Boolean> deleteArticle(@PathVariable Long id) {
        return Result.success(wikiArticleService.removeById(id));
    }
    
    @PostMapping("/articles/{id}/view")
    public Result<Boolean> incrementViewCount(@PathVariable Long id) {
        return Result.success(wikiArticleService.incrementViewCount(id));
    }
}
