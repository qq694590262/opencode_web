package com.opencode.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_log")
public class Log {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String operation;
    
    private String ip;
    
    private String method;
    
    private String params;
    
    private Integer status;
    
    private String errorMsg;
    
    private Long duration;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableLogic
    private Integer deleted;
}
