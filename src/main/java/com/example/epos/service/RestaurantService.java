package com.example.epos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epos.dto.RestaurantDto;
import com.example.epos.entity.Dish;

public interface RestaurantService extends IService<Dish>  {
    //Need handling two tables: dish, dish_flavor
    public void saveWithFlavor(RestaurantDto restaurantDto);
    public void remove(Long id);

    public RestaurantDto getByIdWithFlavor(Long id);
    public int RestaurantStatus(String status);
    public void updateWithFlavor(RestaurantDto restaurantDto);
}
