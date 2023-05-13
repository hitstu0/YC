package com.yecheng.yredis.Util;

import lombok.Data;

@Data
public class RedisConfig {
    private int timeOut;
    private int maxActive;
    private int maxWait;
    private int maxIdle;
}
