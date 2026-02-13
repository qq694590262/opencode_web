package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Log;
import com.opencode.mapper.LogMapper;
import com.opencode.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    
    @Override
    public List<Log> getAllLogs() {
        return this.list();
    }
    
    @Override
    public List<Log> getRecentLogs(int limit) {
        return this.list(null);
    }
    
    @Override
    public boolean saveLog(Log log) {
        return this.save(log);
    }
}
