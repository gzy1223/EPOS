package com.example.epos.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

//global exception handle
@ControllerAdvice(annotations = {RestControllerAdvice.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    //HANDLE THE SQL EXCEPTION
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex)
    {
        log.error(ex.getMessage());
       if(ex.getMessage().contains("Duplicate entry"))
       {
           String [] split = ex.getMessage().split(" ");
           String msg = split[2] + "already exist";
           return R.error(msg);
       }
       return R.error("fail");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex)
    {
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
