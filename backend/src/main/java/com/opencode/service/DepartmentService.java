package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    boolean saveDepartment(Department department);
    boolean updateDepartment(Department department);
    boolean deleteDepartment(Long id);
    List<Department> getDepartmentTree();
}
