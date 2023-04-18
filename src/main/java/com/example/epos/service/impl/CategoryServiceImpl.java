package com.example.epos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epos.common.CustomException;
import com.example.epos.entity.Category;
import com.example.epos.entity.Dish;
import com.example.epos.entity.Setmeal;
import com.example.epos.mapper.CategoryMapper;
import com.example.epos.service.CategoryService;
import com.example.epos.service.DishService;
import com.example.epos.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * @Author: GZY
     * @Description: deleter based on the category
     * @Date: 26/01/2023
     * @Param id:
     * @return: void
     **/
    @Override
    public void remove(Long id)
    {
        //find if it relates the dishes, if is, throw an error
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //add the searching condition
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);

        int count = dishService.count(dishLambdaQueryWrapper);
        //find if it relates the setmeal, if is, throw an error
        if (count>0)
        {
            //related, has an error
            throw new CustomException("Related with the dish, fail to delete");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper =  new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();
        if (count2>0)
        {
            throw new CustomException("Related with the setmeal, fail to delete");
        }
        //delete
        super.removeById(id);
    }
}
