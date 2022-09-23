package com.yecheng.ykv.Exception;

import com.yecheng.ykv.Data.CodeMsg;

public class MyException extends RuntimeException {
    
    private CodeMsg codeMsg;

    public MyException(CodeMsg msg) {
        this.codeMsg = msg;
    }
    
    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
