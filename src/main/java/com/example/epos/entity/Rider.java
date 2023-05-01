package com.example.epos.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rider {
    private String address;
    private BigDecimal earnings;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String riderAvatarUrl;
    private String riderEmail;
    private String riderName;
    private String riderUID;
    private int status;
}
