package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Project;
import com.opencode.mapper.ProjectMapper;
import com.opencode.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    
    @Override
    public List<Project> getAllProjects() {
        return this.list();
    }
    
    @Override
    public Project getProjectById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveProject(Project project) {
        return this.save(project);
    }
    
    @Override
    public boolean updateProject(Project project) {
        return this.updateById(project);
    }
    
    @Override
    public boolean deleteProject(Long id) {
        return this.removeById(id);
    }
}
