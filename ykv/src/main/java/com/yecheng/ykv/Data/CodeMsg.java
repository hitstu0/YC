package com.yecheng.ykv.Data;

import lombok.Data;

@Data
public class CodeMsg {
    private int code;
    private String msg;

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public static CodeMsg SuccessWithData(String data) {
        return new CodeMsg(0, data);
    }

    public static CodeMsg Success = new CodeMsg(0, "success");
    public static CodeMsg NOTMODIFY = new CodeMsg(1, "value not modify");
    public static CodeMsg UserNotLogin = new CodeMsg(100, "user not login");

    public static CodeMsg InternError = new CodeMsg(500, "intern err");
    public static CodeMsg KVNotEXist = new CodeMsg(501, "kv not exist");
    public static CodeMsg KeyShouldChange = new CodeMsg(503, "key should change");
}
