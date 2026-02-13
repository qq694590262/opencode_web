package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Log;
import com.opencode.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;
    private final LogService logService;
    
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        
        // 从数据库统计真实数据
        long totalProjects = projectService.count();
        long pendingTasks = taskService.lambdaQuery().eq(Task::getStatus, "todo").count();
        long activeUsers = userService.lambdaQuery().eq(User::getStatus, 1).count();
        
        data.put("totalVisits", 1234); // 访问量需要统计，暂时用固定值
        data.put("activeUsers", activeUsers);
        data.put("totalProjects", totalProjects);
        data.put("pendingTasks", pendingTasks);
        
        // 生成最近7天的趋势数据
        List<Map<String, Object>> trends = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> trend = new HashMap<>();
            trend.put("day", days[i]);
            trend.put("value", 60 + random.nextInt(40));
            trends.add(trend);
        }
        data.put("trends", trends);
        
        return Result.success(data);
    }
    
    @GetMapping("/activities")
    public Result<List<Map<String, Object>>> getActivities() {
        // 从数据库查询最近的日志作为活动
        List<Log> logs = logService.lambdaQuery()
            .orderByDesc(Log::getCreateTime)
            .last("limit 10")
            .list();
        
        List<Map<String, Object>> activities = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (Log log : logs) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", log.getId());
            activity.put("user", log.getUsername());
            activity.put("action", log.getOperation());
            activity.put("time", formatTimeAgo(log.getCreateTime()));
            activity.put("avatar", "👤");
            activities.add(activity);
        }
        
        return Result.success(activities);
    }
    
    @GetMapping("/quick-links")
    public Result<List<Map<String, String>>> getQuickLinks() {
        List<Map<String, String>> links = new ArrayList<>();
        links.add(Map.of("icon", "➕", "label", "新建项目"));
        links.add(Map.of("icon", "👤", "label", "添加成员"));
        links.add(Map.of("icon", "📄", "label", "上传文档"));
        links.add(Map.of("icon", "📊", "label", "生成报表"));
        links.add(Map.of("icon", "📅", "label", "创建日程"));
        links.add(Map.of("icon", "🔔", "label", "发送通知"));
        
        return Result.success(links);
    }
    
    @GetMapping("/charts")
    public Result<Map<String, Object>> getCharts() {
        Map<String, Object> data = new HashMap<>();
        
        List<Map<String, Object>> pieData = new ArrayList<>();
        pieData.add(Map.of("label", "华东区", "value", 35, "color", "#0ea5e9"));
        pieData.add(Map.of("label", "华南区", "value", 25, "color", "#34d399"));
        pieData.add(Map.of("label", "华北区", "value", 20, "color", "#fbbf24"));
        pieData.add(Map.of("label", "西南区", "value", 15, "color", "#f472b6"));
        pieData.add(Map.of("label", "其他", "value", 5, "color", "#94a3b8"));
        data.put("pieData", pieData);
        
        List<Map<String, Object>> barData = new ArrayList<>();
        barData.add(Map.of("label", "1月", "value", 65));
        barData.add(Map.of("label", "2月", "value", 78));
        barData.add(Map.of("label", "3月", "value", 90));
        barData.add(Map.of("label", "4月", "value", 85));
        barData.add(Map.of("label", "5月", "value", 95));
        barData.add(Map.of("label", "6月", "value", 80));
        data.put("barData", barData);
        
        return Result.success(data);
    }
    
    private String formatTimeAgo(LocalDateTime time) {
        if (time == null) return "";
        
        long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
        if (minutes < 1) return "刚刚";
        if (minutes < 60) return minutes + "分钟前";
        
        long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
        if (hours < 24) return hours + "小时前";
        
        long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
        return days + "天前";
    }
}
