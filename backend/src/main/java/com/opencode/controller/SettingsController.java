package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Settings;
import com.opencode.service.SettingsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    
    private final SettingsService settingsService;
    
    @GetMapping
    public Result<Settings> getSettings(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Settings settings = settingsService.getSettingsByUserId(userId);
            if (settings == null) {
                settings = new Settings();
                settings.setUserId(userId);
                settings.setDarkMode(false);
                settings.setEmailNotify(true);
                settings.setDesktopNotify(false);
                settings.setLanguage("zh-CN");
                settings.setTimezone("Asia/Shanghai");
                settingsService.saveSettings(settings);
            }
            return Result.success(settings);
        }
        return Result.error("未登录");
    }
    
    @PutMapping
    public Result<Boolean> updateSettings(@RequestBody Settings settings, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            settings.setUserId(userId);
            return Result.success(settingsService.updateSettings(settings));
        }
        return Result.error("未登录");
    }
}
