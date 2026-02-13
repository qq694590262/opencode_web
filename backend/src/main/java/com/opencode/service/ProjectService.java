package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Project;

import java.util.List;

public interface ProjectService extends IService<Project> {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    boolean saveProject(Project project);
    boolean updateProject(Project project);
    boolean deleteProject(Long id);
}
