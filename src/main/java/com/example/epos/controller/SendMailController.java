package com.example.epos.controller;

import com.example.epos.entity.MailRequest;
import com.example.epos.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send-mail")
public class SendMailController {
    @Autowired
    private SendMailService sendMailService;
    @GetMapping("/simple")
    public void SendSimpleMessage(@RequestBody MailRequest mailRequest)
    {
        sendMailService.sendSimpleMail(mailRequest);
    }

}
