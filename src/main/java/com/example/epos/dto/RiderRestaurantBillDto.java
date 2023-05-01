package com.example.epos.dto;

import lombok.Data;

import java.time.LocalDateTime;

// get the information of restaurant bill send time and the riders belongings
@Data
public class RiderRestaurantBillDto {
    private String restaurantUid;
    private LocalDateTime lastSendTime;
    private String riderUid;
    private String riderbelong;
    private String billUid;
}
