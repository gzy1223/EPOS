package com.example.epos.common;
/**
  * @Author: GZY
  * @Description: abnormal
  * @Date: 26/01/2023
  * @Param null:
  * @return: null
  **/

public class CustomException extends RuntimeException{
    public CustomException(String message)
    {
        super(message);
    }
}
