package com.example.epos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epos.dto.DishDto;
import com.example.epos.entity.Dish;
import com.example.epos.entity.DishFlavor;

public interface DishService extends IService<Dish>  {
    //Need handling two tables: dish, dish_flavor
    public void saveWithFlavor(DishDto dishDto);
    public void remove(Long id);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
