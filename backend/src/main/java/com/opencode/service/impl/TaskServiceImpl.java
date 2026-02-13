package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Task;
import com.opencode.mapper.TaskMapper;
import com.opencode.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    
    @Override
    public List<Task> getAllTasks() {
        return this.list();
    }
    
    @Override
    public List<Task> getTasksByStatus(String status) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getStatus, status);
        return this.list(wrapper);
    }
    
    @Override
    public boolean saveTask(Task task) {
        return this.save(task);
    }
    
    @Override
    public boolean updateTask(Task task) {
        return this.updateById(task);
    }
    
    @Override
    public boolean deleteTask(Long id) {
        return this.removeById(id);
    }
}
