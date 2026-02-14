package com.opencode.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Log;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService extends IService<Log> {
    List<Log> getAllLogs();
    List<Log> getRecentLogs(int limit);
    boolean saveLog(Log logEntity);
    Page<Log> getLogsPage(int pageNum, int pageSize, String username, String operation, 
                         Integer status, LocalDateTime startTime, LocalDateTime endTime);
    List<Log> searchLogs(String keyword);
    boolean deleteLogs(List<Long> ids);
    boolean clearLogs();
}
