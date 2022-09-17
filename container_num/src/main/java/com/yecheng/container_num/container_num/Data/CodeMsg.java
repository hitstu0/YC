package com.yecheng.container_num.container_num.Data;

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
    
    public static CodeMsg FailWithData(String data) {
        return new CodeMsg(100, data);
    }
    
    public static CodeMsg Success = new CodeMsg(0, "success");
    public static CodeMsg DoNotNeedAdjust = new CodeMsg(1, "nums is same, do not need adjust");

    public static CodeMsg CanNotFindAvaliablePort = new CodeMsg(101, "can not find avaliable port");
    public static CodeMsg InterException = new CodeMsg(200, "inter exception");
}
