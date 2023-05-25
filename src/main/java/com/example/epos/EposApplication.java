package com.example.epos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j // get the log information
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class EposApplication {

    public static void main(String[] args) {
        SpringApplication.run(EposApplication.class, args);
        log.info("launch successfully");

    }

}
