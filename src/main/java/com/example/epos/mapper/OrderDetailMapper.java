package com.example.epos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epos.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
