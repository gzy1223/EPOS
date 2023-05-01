package com.example.epos.entity;

import com.example.epos.dto.OrderDto;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Bill extends OrderDto {
    private String subtitle;
    private String title;
    private String restaurantName;
    private ArrayList<String> productNames;
}
