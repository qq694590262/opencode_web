package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Role;
import com.opencode.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleService roleService;
    
    @GetMapping
    public Result<List<Role>> getAllRoles() {
        return Result.success(roleService.getAllRoles());
    }
    
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable Long id) {
        return Result.success(roleService.getRoleById(id));
    }
    
    @PostMapping
    public Result<Boolean> createRole(@RequestBody Role role) {
        return Result.success(roleService.saveRole(role));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        return Result.success(roleService.updateRole(role));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRole(@PathVariable Long id) {
        return Result.success(roleService.deleteRole(id));
    }
}
