package com.example.epos.dto;

import lombok.Data;

@Data
public class RestaurantSecurity {
    String uid;
    String password;
    String email;
    int status;
}
