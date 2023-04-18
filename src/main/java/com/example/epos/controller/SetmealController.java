package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.dto.SetmealDto;
import com.example.epos.entity.Category;
import com.example.epos.entity.Setmeal;
import com.example.epos.service.CategoryService;
import com.example.epos.service.SetmealDishService;
import com.example.epos.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto)
    {
        log.info("setmeal informatino,{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("Success");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        // Paging constructor object
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // add query criteria, fuzzy query like based on name
        queryWrapper.like(name != null,Setmeal::getName,name);
        // Add sorting criteria to sort by update time in descending order
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        // object copy
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // object copy
            BeanUtils.copyProperties(item,setmealDto);
            //category id
            Long categoryId = item.getCategoryId();
            // Search for category objects by category id
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //Category name
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }
    /**
      * @Author: GZY
      * @Description: delete setmeal
      * @Date: 30/01/2023
      * @Param null:
      * @return: null
      **/

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids)
    {
        log.info("ids:{}",ids);
        setmealService.removeWithDish((ids));
        return R.success("delete success");
    }
    /**
      * @Author: GZY
      * @Description: search the data based on the setmeal data
      * @Date: 01/02/2023

      * @return: com.example.epos.common.R<java.util.List<com.example.epos.entity.Setmeal>>
      **/

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal)
    {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

       List<Setmeal> list = setmealService.list(queryWrapper);

       return R.success(list);
    }
}