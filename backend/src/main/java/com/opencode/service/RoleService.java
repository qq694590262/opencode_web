package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    boolean saveRole(Role role);
    boolean updateRole(Role role);
    boolean deleteRole(Long id);
}
