package com.example.epos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epos.common.CustomException;
import com.example.epos.dto.SetmealDto;
import com.example.epos.entity.Setmeal;
import com.example.epos.entity.SetmealDish;
import com.example.epos.mapper.SetmealMapper;
import com.example.epos.service.SetmealDishService;
import com.example.epos.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //store the basic information of the setmeal and excate the insert
        this.save(setmealDto);

       List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
       setmealDishes.stream().map((item)->{
           item.setSetmealId(setmealDto.getId());
           return item;
       }).collect(Collectors.toList());
        //store the setmal and dishes
        setmealDishService.saveBatch(setmealDishes);
    }
    /**
      * @Author: GZY
      * @Description: delete the dishes
      * @Date: 30/01/2023
      * @Param ids:
      * @return: void
      **/

    @Transactional
    public void removeWithDish(List<Long> ids)
    {
        //search the status to find if can be deleted
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if (count>0)
        {
            throw new CustomException("selling");
        }
        this.removeByIds(ids);
        //if can not deleted, delete the setmeal first
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //set the data in the dish service
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
