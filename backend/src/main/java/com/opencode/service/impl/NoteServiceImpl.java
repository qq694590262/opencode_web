package com.opencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencode.entity.Note;
import com.opencode.mapper.NoteMapper;
import com.opencode.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    
    @Override
    public List<Note> getAllNotes(Long userId) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        if (userId != null && userId > 0) {
            wrapper.eq(Note::getUserId, userId);
        }
        wrapper.orderByDesc(Note::getCreateTime);
        return this.list(wrapper);
    }
    
    @Override
    public boolean saveNote(Note note) {
        return this.save(note);
    }
    
    @Override
    public boolean updateNote(Note note) {
        return this.updateById(note);
    }
    
    @Override
    public boolean deleteNote(Long id) {
        return this.removeById(id);
    }
}
