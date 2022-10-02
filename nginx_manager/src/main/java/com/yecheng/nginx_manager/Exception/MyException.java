package com.yecheng.nginx_manager.Exception;

import com.yecheng.nginx_manager.Data.CodeMsg;

public class MyException extends RuntimeException {
    
    private CodeMsg codeMsg;

    public MyException(CodeMsg msg) {
        this.codeMsg = msg;
    }
    
    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
