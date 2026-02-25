package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Todo;

import java.util.List;

public interface TodoService extends IService<Todo> {
    List<Todo> getAllTodos();
    boolean saveTodo(Todo todo);
    boolean updateTodo(Todo todo);
    boolean deleteTodo(Long id);
}
