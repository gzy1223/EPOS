package com.example.epos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epos.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
