package com.yecheng.ykv.Data;

import lombok.Data;

@Data
public class Kv {
    private int id;
    private String dbkey;
    private String value;
    private int hash;
}
