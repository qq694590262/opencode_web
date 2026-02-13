package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Task;
import com.opencode.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    @GetMapping
    public Result<List<Task>> getAllTasks() {
        return Result.success(taskService.getAllTasks());
    }
    
    @GetMapping("/status/{status}")
    public Result<List<Task>> getTasksByStatus(@PathVariable String status) {
        return Result.success(taskService.getTasksByStatus(status));
    }
    
    @GetMapping("/{id}")
    public Result<Task> getTaskById(@PathVariable Long id) {
        return Result.success(taskService.getById(id));
    }
    
    @PostMapping
    public Result<Boolean> createTask(@RequestBody Task task) {
        return Result.success(taskService.saveTask(task));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return Result.success(taskService.updateTask(task));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTask(@PathVariable Long id) {
        return Result.success(taskService.deleteTask(id));
    }
}
