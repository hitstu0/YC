package com.yecheng.nginx_manager.Data;

import lombok.Data;

@Data
public class CodeMsg<T> {
    private int code;
    private T msg;

    private CodeMsg(int code, T msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public static<T> CodeMsg SuccessWithData(T data) {
        return new CodeMsg<T>(0, data);
    }

    public static CodeMsg<String> Success = new CodeMsg(0, "success");
    
    public static CodeMsg<String> InfoLack = new CodeMsg<String>(100, "service api info lack");
}
