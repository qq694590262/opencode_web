package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.SystemConfig;
import com.opencode.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemConfigController {
    
    private final SystemConfigService systemConfigService;
    
    @GetMapping("/config")
    public Result<SystemConfig> getConfig() {
        return Result.success(systemConfigService.getConfig());
    }
    
    @PutMapping("/config")
    public Result<Boolean> saveConfig(@RequestBody SystemConfig config) {
        return Result.success(systemConfigService.saveConfig(config));
    }
}
