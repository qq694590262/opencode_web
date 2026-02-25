package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Department;
import com.opencode.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    @GetMapping
    public Result<List<Department>> getAllDepartments() {
        return Result.success(departmentService.getAllDepartments());
    }
    
    @GetMapping("/tree")
    public Result<List<Department>> getDepartmentTree() {
        return Result.success(departmentService.getDepartmentTree());
    }
    
    @GetMapping("/{id}")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        return Result.success(departmentService.getDepartmentById(id));
    }
    
    @PostMapping
    public Result<Boolean> createDepartment(@RequestBody Department department) {
        return Result.success(departmentService.saveDepartment(department));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        department.setId(id);
        return Result.success(departmentService.updateDepartment(department));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDepartment(@PathVariable Long id) {
        return Result.success(departmentService.deleteDepartment(id));
    }
}
