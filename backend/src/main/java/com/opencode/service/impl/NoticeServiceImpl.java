package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Notice;
import com.opencode.mapper.NoticeMapper;
import com.opencode.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    
    @Override
    public List<Notice> getNotices(Long userId) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        // 获取针对该用户的通知或全局通知(0)
        wrapper.and(w -> w.eq(Notice::getUserId, userId).or().eq(Notice::getUserId, 0));
        wrapper.orderByDesc(Notice::getCreateTime);
        return this.list(wrapper);
    }
    
    @Override
    public Notice getNoticeById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean saveNotice(Notice notice) {
        return this.save(notice);
    }
    
    @Override
    public boolean updateNotice(Notice notice) {
        return this.updateById(notice);
    }
    
    @Override
    public boolean deleteNotice(Long id) {
        return this.removeById(id);
    }
    
    @Override
    public boolean markAsRead(Long id) {
        Notice notice = new Notice();
        notice.setId(id);
        notice.setIsRead(1);
        return this.updateById(notice);
    }
}
