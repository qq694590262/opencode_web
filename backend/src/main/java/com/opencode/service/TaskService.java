package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    List<Task> getAllTasks();
    List<Task> getTasksByStatus(String status);
    boolean saveTask(Task task);
    boolean updateTask(Task task);
    boolean deleteTask(Long id);
}
