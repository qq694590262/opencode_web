package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.User;
import com.opencode.mapper.UserMapper;
import com.opencode.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getPassword, password)
               .eq(User::getStatus, 1);
        return this.getOne(wrapper);
    }
    
    @Override
    public User getUserInfo(Long userId) {
        return this.getById(userId);
    }
    
    @Override
    public boolean updateUser(User user) {
        return this.updateById(user);
    }
}
