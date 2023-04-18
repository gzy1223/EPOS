package com.example.epos.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private String addressID;
    private boolean isSuccess;
    private String orderBy;
    private String orderId;
    private String orderTime;
    private String paymentDetails;
    private List<String> productIDs;
    private String riderUID;
    private String sellerUID;
    private String status;
    private Long totalAmount;
}
