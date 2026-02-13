package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Log;
import com.opencode.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    
    private final LogService logService;
    
    @GetMapping
    public Result<List<Log>> getAllLogs() {
        return Result.success(logService.getAllLogs());
    }
    
    @GetMapping("/recent")
    public Result<List<Log>> getRecentLogs(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(logService.getRecentLogs(limit));
    }
    
    @PostMapping
    public Result<Boolean> createLog(@RequestBody Log log) {
        return Result.success(logService.saveLog(log));
    }
}
