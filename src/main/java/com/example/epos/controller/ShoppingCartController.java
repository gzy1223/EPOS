package com.example.epos.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.epos.common.BaseContext;
import com.example.epos.common.R;
import com.example.epos.entity.ShoppingCart;
import com.example.epos.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
      * @Author: GZY
      * @Description: add the shopping cart
      * @Date: 01/02/2023
      * @Param shoppingCart:
      * @return: com.example.epos.common.R<com.example.epos.entity.ShoppingCart>
      **/

    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart)
    {
        log.info("Shopping cart:{}",shoppingCart);
        //set the user id
       Long currentId = BaseContext.getCurrentId();
       shoppingCart.setUserId(currentId);
        //if not exist, add one on the original
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null)
        {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        else
        {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //SQL: select * from shopping cart where user_id = ? amd dish_id/ setmeal_id = ?
       ShoppingCart cartResult = shoppingCartService.getOne(queryWrapper);

        if (cartResult != null){
            // if exist, add one
           Integer number = cartResult.getNumber();
           cartResult.setNumber(number+1);
           shoppingCartService.updateById(cartResult);
        }
        else
        {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartResult = shoppingCart;
        }
        //if not add cartlist
        return R.success(cartResult);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list()
    {
        log.info("check the shopping cart");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> clean()
    {
        //SQL: delete from shopping cart where user_id = ?
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);
        return R.success("Success");
    }

}
