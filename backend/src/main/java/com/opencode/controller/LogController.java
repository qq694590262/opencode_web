package com.opencode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencode.common.Result;
import com.opencode.entity.Log;
import com.opencode.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    
    private final LogService logService;
    
    @GetMapping
    public Result<Page<Log>> getLogs(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            Page<Log> page = logService.getLogsPage(pageNum, pageSize, username, operation, status, startTime, endTime);
            return Result.success(page);
        } catch (Exception e) {
            log.error("获取日志列表失败: {}", e.getMessage());
            return Result.error("获取日志失败");
        }
    }
    
    @GetMapping("/all")
    public Result<List<Log>> getAllLogs() {
        try {
            return Result.success(logService.getAllLogs());
        } catch (Exception e) {
            log.error("获取所有日志失败: {}", e.getMessage());
            return Result.error("获取日志失败");
        }
    }
    
    @GetMapping("/recent")
    public Result<List<Log>> getRecentLogs(@RequestParam(defaultValue = "10") int limit) {
        try {
            return Result.success(logService.getRecentLogs(limit));
        } catch (Exception e) {
            log.error("获取最近日志失败: {}", e.getMessage());
            return Result.error("获取日志失败");
        }
    }
    
    @GetMapping("/search")
    public Result<List<Log>> searchLogs(@RequestParam String keyword) {
        try {
            return Result.success(logService.searchLogs(keyword));
        } catch (Exception e) {
            log.error("搜索日志失败: {}", e.getMessage());
            return Result.error("搜索日志失败");
        }
    }
    
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        try {
            long total = logService.count();
            long successCount = logService.count(new LambdaQueryWrapper<Log>().eq(Log::getStatus, 1));
            long failCount = logService.count(new LambdaQueryWrapper<Log>().eq(Log::getStatus, 0));
            
            return Result.success(Map.of(
                "total", total,
                "success", successCount,
                "fail", failCount
            ));
        } catch (Exception e) {
            log.error("获取日志统计失败: {}", e.getMessage());
            return Result.error("获取统计失败");
        }
    }
    
    @PostMapping
    public Result<Boolean> createLog(@RequestBody Log log) {
        try {
            return Result.success(logService.saveLog(log));
        } catch (Exception e) {
            log.error("创建日志失败: {}", e.getMessage());
            return Result.error("创建日志失败");
        }
    }
    
    @DeleteMapping("/{ids}")
    public Result<Boolean> deleteLogs(@PathVariable List<Long> ids) {
        try {
            return Result.success(logService.deleteLogs(ids));
        } catch (Exception e) {
            log.error("删除日志失败: {}", e.getMessage());
            return Result.error("删除日志失败");
        }
    }
    
    @DeleteMapping("/clear")
    public Result<Boolean> clearLogs() {
        try {
            return Result.success(logService.clearLogs());
        } catch (Exception e) {
            log.error("清空日志失败: {}", e.getMessage());
            return Result.error("清空日志失败");
        }
    }
}
