package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.dto.DishDto;
import com.example.epos.entity.Category;
import com.example.epos.entity.Dish;
import com.example.epos.entity.DishFlavor;
import com.example.epos.service.CategoryService;
import com.example.epos.service.DishFlavorService;
import com.example.epos.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;
    //use DTO when the entity object can not follow the required object
    /**
      * @Author: GZY
      * @Description: add the dishes
      * @Date: 28/01/2023
      * @Param dish:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto)
    {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        return R.success("Success");
    }
    @DeleteMapping
    public R<String> delete(Long ids)
    {
        log.info("Delete Dish::{}",ids);
        dishService.remove(ids);
        return R.success("Delete success");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name)
    {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    /**
      * @Author: GZY
      * @Description: search information based on the id
      * @Date: 29/01/2023
      * @Param id:
      * @return: com.example.epos.common.R<com.example.epos.dto.DishDto>
      **/

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id)
    {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    /**
      * @Author: GZY
      * @Description: modification
      * @Date: 29/01/2023
      * @Param dishDto:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto)
    {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return R.success("Success");
    }
    /**
      * @Author: GZY
      * @Description: test version just forget it.
      * @Date: 29/01/2023
      * @Param dish:
      * @return: com.example.epos.common.R<java.util.List<com.example.epos.entity.Dish>>
      **/
//
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish)
//    {
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        //add the condition for the database
//        queryWrapper.eq(Dish::getStatus,1);
//
//        //sort the condition
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list =  dishService.list(queryWrapper);
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish)
    {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //add the condition for the database
        queryWrapper.eq(Dish::getStatus,1);

        //sort the condition
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list =  dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//category id
            //search based on the id
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

           Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper_dto = new LambdaQueryWrapper<>();
            queryWrapper_dto.eq(DishFlavor::getDishId,dishId);
            // it should be SQL: select * from dish_flavor where dish_id = ?
           List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper_dto);
           dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
