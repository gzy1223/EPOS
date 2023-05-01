package com.example.epos.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.entity.Orders;
import com.example.epos.firemapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderContoller {


    /**
      * @Author: GZY
      * @Description: user submit the order
      * @Date: 01/02/2023
      * @Param orders:
      * @return: com.example.epos.common.R<java.lang.String>
      **/


    @GetMapping("/page")
    public R<Page> page( int page,
                         int pageSize,
                         String number) throws ExecutionException, InterruptedException {
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        ArrayList<Orders> ordersArrayList = new ArrayList<>();
        ordersArrayList = OrderMapper.getOrders(number);
        pageInfo.setRecords(ordersArrayList);
        return R.success(pageInfo);
    }
    @PutMapping("")
    public R<Orders> update(@RequestBody Orders orders) throws ExecutionException, InterruptedException {
        OrderMapper.updateOrderStatus(orders);
        return R.success(orders);
    }
}
