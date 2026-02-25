package com.opencode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opencode.entity.Note;

import java.util.List;

public interface NoteService extends IService<Note> {
    List<Note> getAllNotes();
    boolean saveNote(Note note);
    boolean updateNote(Note note);
    boolean deleteNote(Long id);
}
