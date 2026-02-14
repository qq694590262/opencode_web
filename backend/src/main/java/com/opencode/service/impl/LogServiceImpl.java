package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Log;
import com.opencode.mapper.LogMapper;
import com.opencode.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    
    @Override
    public List<Log> getAllLogs() {
        return this.list(new LambdaQueryWrapper<Log>()
            .orderByDesc(Log::getCreateTime));
    }
    
    @Override
    public List<Log> getRecentLogs(int limit) {
        return this.list(new LambdaQueryWrapper<Log>()
            .orderByDesc(Log::getCreateTime)
            .last("LIMIT " + limit));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveLog(Log log) {
        if (log.getCreateTime() == null) {
            log.setCreateTime(LocalDateTime.now());
        }
        if (log.getStatus() == null) {
            log.setStatus(1);
        }
        return this.save(log);
    }
    
    @Override
    public Page<Log> getLogsPage(int pageNum, int pageSize, String username, String operation, 
                                  Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        
        if (username != null && !username.isEmpty()) {
            wrapper.like(Log::getUsername, username);
        }
        if (operation != null && !operation.isEmpty()) {
            wrapper.like(Log::getOperation, operation);
        }
        if (status != null) {
            wrapper.eq(Log::getStatus, status);
        }
        if (startTime != null) {
            wrapper.ge(Log::getCreateTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(Log::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(Log::getCreateTime);
        
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }
    
    @Override
    public List<Log> searchLogs(String keyword) {
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w
                .like(Log::getUsername, keyword)
                .or()
                .like(Log::getOperation, keyword)
                .or()
                .like(Log::getIp, keyword)
            );
        }
        wrapper.orderByDesc(Log::getCreateTime);
        return this.list(wrapper);
    }
    
    @Override
    public boolean deleteLogs(List<Long> ids) {
        return this.removeByIds(ids);
    }
    
    @Override
    public boolean clearLogs() {
        return this.remove(null);
    }
}
