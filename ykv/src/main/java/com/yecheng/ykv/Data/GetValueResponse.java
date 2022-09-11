package com.yecheng.ykv.Data;

import lombok.Data;

@Data
public class GetValueResponse {
    private String value;
    private int hash;
}
