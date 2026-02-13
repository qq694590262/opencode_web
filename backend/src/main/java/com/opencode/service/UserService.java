package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    User getUserInfo(Long userId);
    boolean updateUser(User user);
}
