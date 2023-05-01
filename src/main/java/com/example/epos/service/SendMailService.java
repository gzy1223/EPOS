package com.example.epos.service;

import com.example.epos.dto.Staff;
import com.example.epos.entity.Bill;
import com.example.epos.entity.Employee;
import com.example.epos.entity.MailRequest;

import javax.mail.MessagingException;
import java.io.IOException;

public interface SendMailService {

    /**
     * simple mail request
     *
     * @param mailRequest
     * @return
     */
    void sendSimpleMail(MailRequest mailRequest);
    void passwordEmail(Staff staff, String password) throws MessagingException, IOException;
    void sendInvoicePDF(Bill bill) throws MessagingException, IOException;

    /**
     *html
     *
     * @param mailRequest
     * @return
     */

}

