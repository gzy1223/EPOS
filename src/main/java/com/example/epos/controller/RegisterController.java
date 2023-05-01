package com.example.epos.controller;


import com.example.epos.entity.Restaurant;
import com.example.epos.service.impl.SendMailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api/restaurants")
public class RegisterController {
    @Autowired
    private SendMailServiceImpl sendMailService;
    @PostMapping
    public ResponseEntity<?> registerRestaurant(@RequestBody Restaurant restaurant) throws MessagingException, IOException {
        // Process the Restaurant object (e.g., save it to the database)

        // For demonstration purposes, print the received data
        System.out.println("Received restaurant data: " + restaurant);
        sendMailService.registerEmail(restaurant,"el19zg@leeds.ac.uk");
        return ResponseEntity.ok("Restaurant registered successfully!");
    }
}
