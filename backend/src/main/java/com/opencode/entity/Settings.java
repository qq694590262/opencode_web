package com.opencode.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_settings")
public class Settings {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Boolean darkMode;
    
    private Boolean emailNotify;
    
    private Boolean desktopNotify;
    
    private String language;
    
    private String timezone;
    
    private String bio;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
