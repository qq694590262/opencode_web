package com.opencode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencode.entity.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
}
