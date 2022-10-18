package com.yecheng.log_manager.Data;

import lombok.Data;

@Data
public class LogRequestData {
    //时间
    private long beginTime;
    private long endTime;
    //logId
    private String logId;
    //logger
    private String logger;
    //是否指定前缀查询
    private boolean usePre;

    //关键字
    private String data;
    
}
