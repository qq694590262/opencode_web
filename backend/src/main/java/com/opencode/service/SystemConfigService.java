package com.opencode.service;

import com.opencode.entity.SystemConfig;

public interface SystemConfigService {
    SystemConfig getConfig();
    boolean saveConfig(SystemConfig config);
}
