package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Role;
import com.opencode.mapper.RoleMapper;
import com.opencode.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    
    @Override
    public List<Role> getAllRoles() {
        return this.list();
    }
    
    @Override
    public Role getRoleById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveRole(Role role) {
        return this.save(role);
    }
    
    @Override
    public boolean updateRole(Role role) {
        return this.updateById(role);
    }
    
    @Override
    public boolean deleteRole(Long id) {
        return this.removeById(id);
    }
}
