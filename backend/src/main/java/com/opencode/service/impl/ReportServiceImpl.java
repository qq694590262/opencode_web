package com.opencode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Report;
import com.opencode.mapper.ReportMapper;
import com.opencode.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    
    @Override
    public List<Report> getAllReports() {
        return this.list();
    }
    
    @Override
    public Report getReportById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveReport(Report report) {
        return this.save(report);
    }
    
    @Override
    public boolean updateReport(Report report) {
        return this.updateById(report);
    }
    
    @Override
    public boolean deleteReport(Long id) {
        return this.removeById(id);
    }
}
