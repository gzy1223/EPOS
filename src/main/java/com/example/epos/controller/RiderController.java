package com.example.epos.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.epos.common.R;
import com.example.epos.dto.FirebaseRestaurantDto;
import com.example.epos.dto.RiderBelongDto;
import com.example.epos.entity.Dish;
import com.example.epos.entity.Rider;
import com.example.epos.firemapper.RestaurantMapper;
import com.example.epos.firemapper.RiderMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rider")
public class RiderController {
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) throws ExecutionException, InterruptedException {

        Page<RiderBelongDto> pageInfo = new Page<>(page,pageSize);
        Page<RiderBelongDto> RiderPage = new Page<>();
        ArrayList<RiderBelongDto> ridersArrayList = new ArrayList<>();
        ridersArrayList = RiderMapper.findRiderByName(name);

        RiderPage.setRecords(ridersArrayList);

        return R.success(RiderPage);
    }
    @GetMapping("/list")
    public R<ArrayList> list(String name) throws ExecutionException, InterruptedException {
        ArrayList<RiderBelongDto> RiderArrayList = new ArrayList<>();
        RiderArrayList = RestaurantMapper.getRestaurant(name);
        return R.success(RiderArrayList);
    }
    @PostMapping("")
    public R<String> update(String riderName,long belonging) throws ExecutionException, InterruptedException {
        ArrayList<String> sellerArrayList = new ArrayList<>();
        String name = "";
        sellerArrayList = RestaurantMapper.getRestaurantUId(name);
        String sellerUID = sellerArrayList.get((int) belonging);
        RiderMapper.updateBelonging(riderName,sellerUID);
        return R.success("Success");
    }
}
