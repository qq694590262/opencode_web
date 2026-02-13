package com.opencode.controller;

import com.opencode.entity.User;
import com.opencode.service.UserService;
import com.opencode.common.Result;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");
        
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", "mock-token-" + user.getId());
            data.put("user", user);
            return Result.success(data);
        }
        return Result.error("用户名或密码错误");
    }
    
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success(null);
    }
    
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserInfo(userId);
            return Result.success(user);
        }
        return Result.error("未登录");
    }
}
