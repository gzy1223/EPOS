package com.example.epos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epos.entity.Category;


public interface CategoryService  extends IService<Category> {
    public void remove(Long id);
}
