package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.User;
import com.opencode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.list());
    }
    
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }
    
    @PostMapping
    public Result<Boolean> createUser(@RequestBody User user) {
        return Result.success(userService.save(user));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return Result.success(userService.updateUser(user));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }
}
