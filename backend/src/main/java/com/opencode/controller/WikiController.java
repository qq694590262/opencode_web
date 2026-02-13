package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.WikiCategory;
import com.opencode.service.WikiCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wiki")
@RequiredArgsConstructor
public class WikiController {
    
    private final WikiCategoryService wikiCategoryService;
    
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
}
