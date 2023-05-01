package com.example.epos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class FirebaseRestaurantDto {
    private String address;
    private BigDecimal earnings;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String postalCode;
    private String sellerAvatarUrl;
    private String sellerEmail;
    private String sellerName;
    private String sellerUID;
    private String status;
}
