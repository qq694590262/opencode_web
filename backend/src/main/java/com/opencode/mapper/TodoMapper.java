package com.opencode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencode.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper extends BaseMapper<Todo> {
}
