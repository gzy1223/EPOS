package com.example.epos.service.impl;

import com.example.epos.dto.Staff;
import com.example.epos.entity.Bill;
import com.example.epos.entity.Employee;
import com.example.epos.entity.MailRequest;
import com.example.epos.entity.Restaurant;
import com.example.epos.service.SendMailService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class SendMailServiceImpl implements SendMailService {

    //注入邮件工具类
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PDFServiceImpl pdfService;
    @Value("${spring.mail.username}")
    private String sendMailer;

    private static final Logger logger = LoggerFactory.getLogger(SendMailServiceImpl.class);

    public void checkMail(MailRequest mailRequest) {
        Assert.notNull(mailRequest,"null");
        Assert.notNull(mailRequest.getSendTo(), "null");
        Assert.notNull(mailRequest.getSubject(), "null");
        Assert.notNull(mailRequest.getText(), "null");
    }

    @Override
    public void sendSimpleMail(MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        checkMail(mailRequest);
        //邮件发件人
        message.setFrom(sendMailer);
        //邮件收件人 1或多个
        message.setTo(mailRequest.getSendTo().split(","));
        //邮件主题
        message.setSubject(mailRequest.getSubject());
        //邮件内容
        message.setText(mailRequest.getText());
        //邮件发送时间
        message.setSentDate(new Date());

        javaMailSender.send(message);
        logger.info("success:{}->{}",sendMailer,mailRequest.getSendTo());
    }
    public void registerEmail(Restaurant restaurant,String hostEmail) throws IOException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //邮件发件人
        helper.setFrom(sendMailer);
        String [] receivers = {restaurant.getEmail(),hostEmail};
        //邮件收件人 1或多个
        helper.setTo(receivers);
        //邮件主题
        helper.setSubject("Register Request");
        //邮件内容
        helper.setText("A request from "+restaurant.getName());

        //邮件发送时间
        message.setSentDate(new Date());

        PDDocument pdf = pdfService.RegisterPDF(restaurant);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdf.save(byteArrayOutputStream);
        ByteArrayResource pdfResource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        helper.addAttachment("Register.pdf",pdfResource);
        javaMailSender.send(message);
    }
    public void passwordEmail(Staff staff,String password) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //邮件发件人
        helper.setFrom(sendMailer);
        String [] receivers = {staff.getEmail()};
        //邮件收件人 1或多个
        helper.setTo(receivers);
        //邮件主题
        helper.setSubject("Your password");
        //邮件内容
        helper.setText("The password for staff"+staff.getUsername());

        //邮件发送时间
        message.setSentDate(new Date());

        PDDocument pdf = pdfService.PasswordPDF(password);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdf.save(byteArrayOutputStream);
        ByteArrayResource pdfResource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        helper.addAttachment("password.pdf",pdfResource);
        javaMailSender.send(message);
    }
    @Override
    public void sendInvoicePDF(Bill bill) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //邮件发件人
        helper.setFrom(sendMailer);

        //邮件收件人 1或多个
        helper.setTo("gzy7189@gmail.com");
        //邮件主题
        helper.setSubject("Your invoice");
        //邮件内容
        helper.setText("Your invoice");

        //邮件发送时间
        message.setSentDate(new Date());

        PDDocument pdf = pdfService.OrderPDF(bill);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdf.save(byteArrayOutputStream);
        ByteArrayResource pdfResource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        helper.addAttachment("invoice.pdf",pdfResource);
        javaMailSender.send(message);
    }
}


