package com.example.epos.entity;

import lombok.Data;

import java.io.Serializable;

@Data
    public class Restaurant implements Serializable {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
}
