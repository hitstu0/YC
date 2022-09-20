package com.yecheng.ykv.Data;

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
    public static CodeMsg<String> NOTMODIFY = new CodeMsg(1, "value not modify");
    public static CodeMsg<String> UserNotLogin = new CodeMsg(100, "user not login");

    public static CodeMsg<String> InternError = new CodeMsg(500, "intern err");
    public static CodeMsg<String> KVNotEXist = new CodeMsg(501, "kv not exist");
    public static CodeMsg<String> KeyShouldChange = new CodeMsg(503, "key should change");
}
