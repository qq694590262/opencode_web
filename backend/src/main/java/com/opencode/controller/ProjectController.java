package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Project;
import com.opencode.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    
    @GetMapping
    public Result<List<Project>> getAllProjects() {
        return Result.success(projectService.getAllProjects());
    }
    
    @GetMapping("/{id}")
    public Result<Project> getProjectById(@PathVariable Long id) {
        return Result.success(projectService.getProjectById(id));
    }
    
    @PostMapping
    public Result<Boolean> createProject(@RequestBody Project project) {
        return Result.success(projectService.saveProject(project));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        return Result.success(projectService.updateProject(project));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProject(@PathVariable Long id) {
        return Result.success(projectService.deleteProject(id));
    }
}
