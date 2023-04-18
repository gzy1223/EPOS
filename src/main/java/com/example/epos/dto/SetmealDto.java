package com.example.epos.dto;


import com.example.epos.entity.Setmeal;
import com.example.epos.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
