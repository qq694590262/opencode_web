package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Todo;
import com.opencode.mapper.TodoMapper;
import com.opencode.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {
    
    @Override
    public List<Todo> getAllTodos() {
        return this.list();
    }
    
    @Override
    public boolean saveTodo(Todo todo) {
        return this.save(todo);
    }
    
    @Override
    public boolean updateTodo(Todo todo) {
        return this.updateById(todo);
    }
    
    @Override
    public boolean deleteTodo(Long id) {
        return this.removeById(id);
    }
}
