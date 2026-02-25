package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Department;
import com.opencode.mapper.DepartmentMapper;
import com.opencode.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    
    @Override
    public List<Department> getAllDepartments() {
        return this.list();
    }
    
    @Override
    public Department getDepartmentById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveDepartment(Department department) {
        return this.save(department);
    }
    
    @Override
    public boolean updateDepartment(Department department) {
        return this.updateById(department);
    }
    
    @Override
    public boolean deleteDepartment(Long id) {
        // 检查是否有子部门
        long childCount = this.lambdaQuery()
                .eq(Department::getParentId, id)
                .count();
        if (childCount > 0) {
            throw new RuntimeException("该部门存在子部门，无法删除");
        }
        return this.removeById(id);
    }
    
    @Override
    public List<Department> getDepartmentTree() {
        List<Department> allDepts = this.list();
        return buildTree(allDepts, null);
    }
    
    private List<Department> buildTree(List<Department> allDepts, Long parentId) {
        List<Department> result = new ArrayList<>();
        for (Department dept : allDepts) {
            boolean isRoot = parentId == null && (dept.getParentId() == null || dept.getParentId() == 0);
            boolean isChild = parentId != null && parentId.equals(dept.getParentId());
            
            if (isRoot || isChild) {
                List<Department> children = buildTree(allDepts, dept.getId());
                if (!children.isEmpty()) {
                    dept.setChildren(children);
                }
                result.add(dept);
            }
        }
        return result;
    }
}
