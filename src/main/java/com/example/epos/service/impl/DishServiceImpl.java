package com.example.epos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.epos.common.CustomException;
import com.example.epos.dto.DishDto;
import com.example.epos.entity.Dish;
import com.example.epos.entity.DishFlavor;
import com.example.epos.entity.Setmeal;
import com.example.epos.mapper.DishMapper;
import com.example.epos.service.DishFlavorService;
import com.example.epos.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;
    /**
     * @Author: GZY
     * @Description: add dishes
     * @Date: 28/01/2023
     * @Param dishDto:
     * @return: void
     **/
    //multiple table
    @Transactional
    public void saveWithFlavor(DishDto dishDto)
    {
        this.save(dishDto);
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        // lambda to handle the stream
        //save flavor to dish flavor
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
    @Override
    public void remove(Long ids)
    {
        //find if it relates the dishes, if is, throw an error
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //add the searching condition
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        //delete
        super.removeById(ids);
    }
    /**
      * @Author: GZY
      * @Description: find the flavor and information based on the id
      * @Date: 29/01/2023
      * @Param id:
      * @return: com.example.epos.dto.DishDto
      **/

    public DishDto getByIdWithFlavor(Long id)
    {
        //search basic information from the dish table
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        // search the id
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto)
    {
        //update the dish table information
        this.updateById(dishDto);
        //clean the current flavor data
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);
        //add a newly submitted flavor data
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
