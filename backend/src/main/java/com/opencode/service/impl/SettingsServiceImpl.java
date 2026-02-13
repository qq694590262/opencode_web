package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Settings;
import com.opencode.mapper.SettingsMapper;
import com.opencode.service.SettingsService;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsMapper, Settings> implements SettingsService {
    
    @Override
    public Settings getSettingsByUserId(Long userId) {
        LambdaQueryWrapper<Settings> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Settings::getUserId, userId);
        return this.getOne(wrapper);
    }
    
    @Override
    public boolean saveSettings(Settings settings) {
        return this.save(settings);
    }
    
    @Override
    public boolean updateSettings(Settings settings) {
        return this.updateById(settings);
    }
}
