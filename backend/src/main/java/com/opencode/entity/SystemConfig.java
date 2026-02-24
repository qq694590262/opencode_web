package com.opencode.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_system_config")
public class SystemConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String systemName;
    
    private String copyright;
    
    private Integer loginTimeout;
    
    private Integer passwordExpire;
    
    private Integer maxLoginFail;
    
    private Integer maxDevices;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
