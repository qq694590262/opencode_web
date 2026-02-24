package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.opencode.entity.SystemConfig;
import com.opencode.mapper.SystemConfigMapper;
import com.opencode.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigMapper systemConfigMapper;

    @Override
    public SystemConfig getConfig() {
        List<SystemConfig> configs = systemConfigMapper.selectList(new LambdaQueryWrapper<>());
        if (configs != null && !configs.isEmpty()) {
            return configs.get(0);
        }
        // 返回默认配置
        SystemConfig defaultConfig = new SystemConfig();
        defaultConfig.setSystemName("OpenCode 企业管理系统");
        defaultConfig.setCopyright("© 2024 OpenCode. All rights reserved.");
        defaultConfig.setLoginTimeout(30);
        defaultConfig.setPasswordExpire(90);
        defaultConfig.setMaxLoginFail(5);
        defaultConfig.setMaxDevices(3);
        systemConfigMapper.insert(defaultConfig);
        return defaultConfig;
    }

    @Override
    public boolean saveConfig(SystemConfig config) {
        List<SystemConfig> configs = systemConfigMapper.selectList(new LambdaQueryWrapper<>());
        if (configs != null && !configs.isEmpty()) {
            config.setId(configs.get(0).getId());
            return systemConfigMapper.updateById(config) > 0;
        } else {
            return systemConfigMapper.insert(config) > 0;
        }
    }
}
