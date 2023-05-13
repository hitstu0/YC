package com.yecheng.api_gateway.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.api_gateway.Data.CodeMsg;

@ControllerAdvice
public class MyExceptionHandler {
    
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public CodeMsg handel(MyException e) {
        return e.getCodeMsg();
    }
}