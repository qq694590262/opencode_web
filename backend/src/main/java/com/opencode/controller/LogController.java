package com.opencode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencode.common.Result;
import com.opencode.entity.Log;
import com.opencode.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        Page<Log> page = logService.getLogsPage(pageNum, pageSize, username, operation, status, startTime, endTime);
        return Result.success(page);
    }
    
    @GetMapping("/all")
    public Result<List<Log>> getAllLogs() {
        return Result.success(logService.getAllLogs());
    }
    
    @GetMapping("/recent")
    public Result<List<Log>> getRecentLogs(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(logService.getRecentLogs(limit));
    }
    
    @GetMapping("/search")
    public Result<List<Log>> searchLogs(@RequestParam String keyword) {
        return Result.success(logService.searchLogs(keyword));
    }
    
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        long total = logService.count();
        long successCount = logService.count(new LambdaQueryWrapper<Log>().eq(Log::getStatus, 1));
        long failCount = logService.count(new LambdaQueryWrapper<Log>().eq(Log::getStatus, 0));
        
        return Result.success(Map.of(
            "total", total,
            "success", successCount,
            "fail", failCount
        ));
    }
    
    @PostMapping
    public Result<Boolean> createLog(@RequestBody Log log) {
        return Result.success(logService.saveLog(log));
    }
    
    @DeleteMapping("/{ids}")
    public Result<Boolean> deleteLogs(@PathVariable List<Long> ids) {
        return Result.success(logService.deleteLogs(ids));
    }
    
    @DeleteMapping("/clear")
    public Result<Boolean> clearLogs() {
        return Result.success(logService.clearLogs());
    }
}
