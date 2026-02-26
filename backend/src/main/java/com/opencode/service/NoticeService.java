package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    List<Notice> getNotices(Long userId);
    Notice getNoticeById(Long id);
    boolean saveNotice(Notice notice);
    boolean updateNotice(Notice notice);
    boolean deleteNotice(Long id);
    boolean markAsRead(Long id);
}
