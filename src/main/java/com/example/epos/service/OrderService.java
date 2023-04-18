package com.example.epos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.epos.entity.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
