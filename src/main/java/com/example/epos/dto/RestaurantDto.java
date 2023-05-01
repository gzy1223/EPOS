package com.example.epos.dto;

import com.example.epos.entity.Dish;
import com.example.epos.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
//needs to be modified in the future
@Data
public class RestaurantDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
