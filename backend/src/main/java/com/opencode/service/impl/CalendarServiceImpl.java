package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Calendar;
import com.opencode.mapper.CalendarMapper;
import com.opencode.service.CalendarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, Calendar> implements CalendarService {
    
    @Override
    public List<Calendar> getAllCalendars() {
        return this.list();
    }
    
    @Override
    public List<Calendar> getCalendarsByUserId(Long userId) {
        LambdaQueryWrapper<Calendar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Calendar::getUserId, userId);
        return this.list(wrapper);
    }
    
    @Override
    public boolean saveCalendar(Calendar calendar) {
        return this.save(calendar);
    }
    
    @Override
    public boolean updateCalendar(Calendar calendar) {
        return this.updateById(calendar);
    }
    
    @Override
    public boolean deleteCalendar(Long id) {
        return this.removeById(id);
    }
}
