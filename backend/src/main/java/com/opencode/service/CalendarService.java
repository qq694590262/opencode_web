package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Calendar;

import java.util.List;

public interface CalendarService extends IService<Calendar> {
    List<Calendar> getAllCalendars();
    List<Calendar> getCalendarsByUserId(Long userId);
    boolean saveCalendar(Calendar calendar);
    boolean updateCalendar(Calendar calendar);
    boolean deleteCalendar(Long id);
}
