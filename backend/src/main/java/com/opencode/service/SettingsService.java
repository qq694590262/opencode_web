package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Settings;

public interface SettingsService extends IService<Settings> {
    Settings getSettingsByUserId(Long userId);
    boolean saveSettings(Settings settings);
    boolean updateSettings(Settings settings);
}
