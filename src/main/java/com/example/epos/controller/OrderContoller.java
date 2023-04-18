package com.example.epos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.entity.Orders;
import com.example.epos.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderContoller {
    @Autowired
    private OrderService orderService;

    /**
      * @Author: GZY
      * @Description: user submit the order
      * @Date: 01/02/2023
      * @Param orders:
      * @return: com.example.epos.common.R<java.lang.String>
      **/

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders)
    {
        orderService.submit(orders);
        return R.success("success put the order");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize)
    {
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}
