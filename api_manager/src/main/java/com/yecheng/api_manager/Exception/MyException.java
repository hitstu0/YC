package com.yecheng.api_manager.Exception;

import com.yecheng.api_manager.Data.CodeMsg;

public class MyException extends RuntimeException {
    
    private CodeMsg codeMsg;

    public MyException(CodeMsg msg) {
        this.codeMsg = msg;
    }
    
    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
