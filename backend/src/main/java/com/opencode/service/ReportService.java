package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Report;

import java.util.List;

public interface ReportService extends IService<Report> {
    List<Report> getAllReports();
    Report getReportById(Long id);
    boolean saveReport(Report report);
    boolean updateReport(Report report);
    boolean deleteReport(Long id);
}
