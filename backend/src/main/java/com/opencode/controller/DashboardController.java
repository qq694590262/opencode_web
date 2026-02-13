package com.opencode.controller;

import com.opencode.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        
        data.put("totalVisits", 1234);
        data.put("activeUsers", 856);
        data.put("totalProjects", 328);
        data.put("pendingTasks", 96);
        
        List<Map<String, Object>> trends = new ArrayList<>();
        trends.add(Map.of("day", "周一", "value", 65));
        trends.add(Map.of("day", "周二", "value", 78));
        trends.add(Map.of("day", "周三", "value", 90));
        trends.add(Map.of("day", "周四", "value", 85));
        trends.add(Map.of("day", "周五", "value", 95));
        trends.add(Map.of("day", "周六", "value", 80));
        trends.add(Map.of("day", "周日", "value", 70));
        data.put("trends", trends);
        
        return Result.success(data);
    }
    
    @GetMapping("/activities")
    public Result<List<Map<String, Object>>> getActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        activities.add(Map.of("id", 1, "user", "张三", "action", "完成了项目\"企业管理系统\"的开发", "time", "5分钟前", "avatar", "👤"));
        activities.add(Map.of("id", 2, "user", "李四", "action", "上传了新文档《技术方案v2.0》", "time", "15分钟前", "avatar", "👤"));
        activities.add(Map.of("id", 3, "user", "王五", "action", "创建了新任务\"系统优化\"", "time", "1小时前", "avatar", "👤"));
        activities.add(Map.of("id", 4, "user", "赵六", "action", "更新了用户权限配置", "time", "2小时前", "avatar", "👤"));
        activities.add(Map.of("id", 5, "user", "钱七", "action", "完成了本周报表汇总", "time", "3小时前", "avatar", "👤"));
        
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
}
