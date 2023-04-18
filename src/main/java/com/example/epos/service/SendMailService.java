package com.example.epos.service;

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
    void loginHint(Employee employee) throws IOException, MessagingException;

    /**
     *html
     *
     * @param mailRequest
     * @return
     */

}

