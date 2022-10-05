package com.yecheng.log_manager.Data;

import lombok.Data;

@Data
public class LogData {
    private long logIdTime;
    private String logIdTag;
    private long logTime;
    private String data;
}
