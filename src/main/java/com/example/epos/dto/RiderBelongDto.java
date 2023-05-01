package com.example.epos.dto;

import com.example.epos.entity.Rider;
import lombok.Data;

@Data
public class RiderBelongDto extends Rider {
    private String belonging;
}
