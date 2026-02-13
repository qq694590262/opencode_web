package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Log;

import java.util.List;

public interface LogService extends IService<Log> {
    List<Log> getAllLogs();
    List<Log> getRecentLogs(int limit);
    boolean saveLog(Log log);
}
