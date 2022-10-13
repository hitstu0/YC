package com.yecheng.log_manager.Data;

import lombok.Data;

@Data
public class LogData {
    private long logTime; //打印该日志的时间，用于查看或链路查询排列

    private String level;  //日志级别，用于根据级别搜索
 
    private String logger; //打印日志的类，用于搜索
    private String line;      //打印行，用于查看

    private String logId;  //logID

    private String data; //日志数据
}
