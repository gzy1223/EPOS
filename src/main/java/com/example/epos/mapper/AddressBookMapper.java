package com.example.epos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.epos.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper  extends BaseMapper<AddressBook> {
}
