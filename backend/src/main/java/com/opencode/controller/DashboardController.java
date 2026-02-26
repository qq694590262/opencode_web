package com.opencode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.opencode.common.Result;
import com.opencode.entity.*;
import com.opencode.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private final NoteService noteService;
    private final TodoService todoService;
    private final NoticeService noticeService;
    
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        
        long totalProjects = projectService.count();
        
        LambdaQueryWrapper<Task> taskWrapper = new LambdaQueryWrapper<>();
        taskWrapper.eq(Task::getStatus, "todo");
        long pendingTasks = taskService.count(taskWrapper);
        
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getStatus, 1);
        long activeUsers = userService.count(userWrapper);
        
        data.put("totalVisits", 1234);
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
        LambdaQueryWrapper<Log> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.orderByDesc(Log::getCreateTime);
        logWrapper.last("limit 10");
        List<Log> logs = logService.list(logWrapper);
        
        List<Map<String, Object>> activities = new ArrayList<>();
        
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
        links.add(createLink("➕", "新建项目"));
        links.add(createLink("👤", "添加成员"));
        links.add(createLink("📄", "上传文档"));
        links.add(createLink("📊", "生成报表"));
        links.add(createLink("📅", "创建日程"));
        links.add(createLink("🔔", "发送通知"));
        
        return Result.success(links);
    }
    
    private Map<String, String> createLink(String icon, String label) {
        Map<String, String> link = new HashMap<>();
        link.put("icon", icon);
        link.put("label", label);
        return link;
    }
    
    @GetMapping("/charts")
    public Result<Map<String, Object>> getCharts() {
        Map<String, Object> data = new HashMap<>();
        
        LambdaQueryWrapper<Project> activeProjectWrapper = new LambdaQueryWrapper<>();
        activeProjectWrapper.eq(Project::getStatus, "active");
        long activeProjects = projectService.count(activeProjectWrapper);
        
        LambdaQueryWrapper<Project> pendingProjectWrapper = new LambdaQueryWrapper<>();
        pendingProjectWrapper.eq(Project::getStatus, "pending");
        long pendingProjects = projectService.count(pendingProjectWrapper);
        
        LambdaQueryWrapper<Project> doneProjectWrapper = new LambdaQueryWrapper<>();
        doneProjectWrapper.eq(Project::getStatus, "done");
        long doneProjects = projectService.count(doneProjectWrapper);
        
        List<Map<String, Object>> pieData = new ArrayList<>();
        pieData.add(createChartData("进行中", (int) activeProjects, "#0ea5e9"));
        pieData.add(createChartData("待开始", (int) pendingProjects, "#fbbf24"));
        pieData.add(createChartData("已完成", (int) doneProjects, "#34d399"));
        data.put("pieData", pieData);
        
        // 生成月度数据
        List<Map<String, Object>> barData = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月"};
        Random random = new Random();
        for (String month : months) {
            barData.add(createChartData(month, 60 + random.nextInt(40), null));
        }
        data.put("barData", barData);
        
        return Result.success(data);
    }
    
    private Map<String, Object> createChartData(String label, int value, String color) {
        Map<String, Object> data = new HashMap<>();
        data.put("label", label);
        data.put("value", value);
        if (color != null) {
            data.put("color", color);
        }
        return data;
    }
    
    @GetMapping("/system-info")
    public Result<Map<String, Object>> getSystemInfo() {
        Map<String, Object> data = new HashMap<>();
        
        data.put("status", "运行中");
        data.put("uptime", "15天6小时");
        data.put("onlineUsers", 12);
        data.put("dbStatus", "正常");
        
        data.put("cpuUsage", 45);
        data.put("memoryUsage", 62);
        data.put("memoryUsed", "4.2");
        data.put("memoryTotal", "8");
        data.put("diskUsage", 38);
        data.put("diskUsed", "76");
        data.put("diskTotal", "200");
        data.put("jvmUsage", 55);
        data.put("jvmUsed", "512");
        data.put("jvmTotal", "1024");
        
        data.put("version", "v1.0.0");
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("os", System.getProperty("os.name"));
        data.put("database", "MySQL 8.0.33");
        data.put("timezone", "Asia/Shanghai");
        
        return Result.success(data);
    }
    
    // ========== 便签 API ==========
    @GetMapping("/notes")
    public Result<List<Note>> getNotes(@RequestParam(required = false, defaultValue = "1") Long userId) {
        List<Note> notes = noteService.getAllNotes(userId);
        return Result.success(notes);
    }
    
    @PostMapping("/notes")
    public Result<Boolean> saveNote(@RequestBody Note note, @RequestParam(required = false, defaultValue = "1") Long userId) {
        note.setUserId(userId);
        boolean result = noteService.saveNote(note);
        return Result.success(result);
    }
    
    @PutMapping("/notes/{id}")
    public Result<Boolean> updateNote(@PathVariable Long id, @RequestBody Note note) {
        note.setId(id);
        boolean result = noteService.updateNote(note);
        return Result.success(result);
    }
    
    @DeleteMapping("/notes/{id}")
    public Result<Boolean> deleteNote(@PathVariable Long id) {
        boolean result = noteService.deleteNote(id);
        return Result.success(result);
    }
    
    // ========== 待办事项 API ==========
    @GetMapping("/todos")
    public Result<List<Todo>> getTodos(@RequestParam(required = false, defaultValue = "1") Long userId) {
        List<Todo> todos = todoService.getAllTodos(userId);
        return Result.success(todos);
    }
    
    @PostMapping("/todos")
    public Result<Boolean> saveTodo(@RequestBody Todo todo, @RequestParam(required = false, defaultValue = "1") Long userId) {
        todo.setUserId(userId);
        boolean result = todoService.saveTodo(todo);
        return Result.success(result);
    }
    
    @PutMapping("/todos/{id}")
    public Result<Boolean> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        boolean result = todoService.updateTodo(todo);
        return Result.success(result);
    }
    
    @DeleteMapping("/todos/{id}")
    public Result<Boolean> deleteTodo(@PathVariable Long id) {
        boolean result = todoService.deleteTodo(id);
        return Result.success(result);
    }
    
    // ========== 公告 API ==========
    @GetMapping("/notices")
    public Result<List<Notice>> getNotices(@RequestParam(required = false, defaultValue = "1") Long userId) {
        List<Notice> notices = noticeService.getNotices(userId);
        return Result.success(notices);
    }
    
    @GetMapping("/notices/{id}")
    public Result<Notice> getNoticeById(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);
        return Result.success(notice);
    }
    
    @PostMapping("/notices")
    public Result<Boolean> saveNotice(@RequestBody Notice notice) {
        boolean result = noticeService.saveNotice(notice);
        return Result.success(result);
    }
    
    @PutMapping("/notices/{id}")
    public Result<Boolean> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        notice.setId(id);
        boolean result = noticeService.updateNotice(notice);
        return Result.success(result);
    }
    
    @DeleteMapping("/notices/{id}")
    public Result<Boolean> deleteNotice(@PathVariable Long id) {
        boolean result = noticeService.deleteNotice(id);
        return Result.success(result);
    }
    
    @PutMapping("/notices/{id}/read")
    public Result<Boolean> markNoticeAsRead(@PathVariable Long id) {
        boolean result = noticeService.markAsRead(id);
        return Result.success(result);
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
