package com.example.epos.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.dto.RestaurantDto;
import com.example.epos.entity.*;
import com.example.epos.firemapper.RestaurantMapper;
import com.example.epos.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

import java.util.ArrayList;

import java.util.concurrent.ExecutionException;


import static com.example.epos.firemapper.OrderMapper.getOrderByname;
import static com.example.epos.firemapper.OrderMapper.getOrderwithinOneweek;
import static com.example.epos.firemapper.RestaurantMapper.changeRestaurantStatus;

@RestController
@RequestMapping("/dish")
@Slf4j
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private SendMailService sendMailService;


    //use DTO when the entity object can not follow the required object
    /**
      * @Author: GZY
      * @Description: add the dishes
      * @Date: 28/01/2023
      * @Param dish:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    @PutMapping
    public R<String> save(@RequestBody RestaurantDto restaurantDto) throws ExecutionException, InterruptedException {
        RestaurantMapper.addRestaurant(restaurantDto);

        return R.success("Success");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name) throws ExecutionException, InterruptedException {

        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<RestaurantDto> dishDtoPage = new Page<>();
        ArrayList<RestaurantDto> restaurantDtoArrayList = new ArrayList<>();
        restaurantDtoArrayList = RestaurantMapper.getRestaurant(name);

        dishDtoPage.setRecords(restaurantDtoArrayList);

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
    public R<RestaurantDto> get(@PathVariable Long id)
    {
        RestaurantDto restaurantDto = restaurantService.getByIdWithFlavor(id);
        return R.success(restaurantDto);
    }
    /**
      * @Author: GZY
      * @Description: modification
      * @Date: 29/01/2023
      * @Param dishDto:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

//    @PutMapping
//    public R<String> update(@RequestBody RestaurantDto restaurantDto)
//    {
//        log.info(restaurantDto.toString());
//        restaurantService.updateWithFlavor(restaurantDto);
//
//        return R.success("Success");
//    }
    @PostMapping("/status/{name}")
    public R<String> updateStatus(String name) throws ExecutionException, InterruptedException {
        changeRestaurantStatus(name);
        return R.success("Success");
    }
    @PostMapping("/sendbill")
    public R<String> sendBill(@RequestParam("name") String name)throws ExecutionException, InterruptedException, MessagingException, IOException {
        //get current time
        ArrayList<Bill> billArrayList = new ArrayList<>();
        billArrayList = getOrderByname(name);
        sendMailService.sendInvoicePDF(billArrayList.get(0));
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


}
