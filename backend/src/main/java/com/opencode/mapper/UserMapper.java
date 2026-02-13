package com.opencode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencode.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
