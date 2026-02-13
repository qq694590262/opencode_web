package com.opencode.controller;

import com.opencode.common.Result;
import com.opencode.entity.Calendar;
import com.opencode.service.CalendarService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    
    private final CalendarService calendarService;
    
    @GetMapping
    public Result<List<Calendar>> getAllCalendars() {
        return Result.success(calendarService.getAllCalendars());
    }
    
    @GetMapping("/user")
    public Result<List<Calendar>> getCalendarsByUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(calendarService.getCalendarsByUserId(userId));
    }
    
    @GetMapping("/{id}")
    public Result<Calendar> getCalendarById(@PathVariable Long id) {
        return Result.success(calendarService.getById(id));
    }
    
    @PostMapping
    public Result<Boolean> createCalendar(@RequestBody Calendar calendar, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        calendar.setUserId(userId);
        return Result.success(calendarService.saveCalendar(calendar));
    }
    
    @PutMapping("/{id}")
    public Result<Boolean> updateCalendar(@PathVariable Long id, @RequestBody Calendar calendar) {
        calendar.setId(id);
        return Result.success(calendarService.updateCalendar(calendar));
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCalendar(@PathVariable Long id) {
        return Result.success(calendarService.deleteCalendar(id));
    }
}
