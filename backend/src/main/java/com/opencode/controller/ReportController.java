package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Report;
import com.opencode.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    
    private final ReportService reportService;
    
    @GetMapping
    public Result<List<Report>> getAllReports() {
        return Result.success(reportService.getAllReports());
    }
    
    @GetMapping("/{id}")
    public Result<Report> getReportById(@PathVariable Long id) {
        return Result.success(reportService.getReportById(id));
    }
    
    @PostMapping
    public Result<Boolean> createReport(@RequestBody Report report) {
        return Result.success(reportService.saveReport(report));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateReport(@PathVariable Long id, @RequestBody Report report) {
        report.setId(id);
        return Result.success(reportService.updateReport(report));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteReport(@PathVariable Long id) {
        return Result.success(reportService.deleteReport(id));
    }
}
