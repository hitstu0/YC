package com.yecheng.log_manager.Mybatis.Mapper;

import java.util.List;

import com.yecheng.log_manager.Data.LogData;
import com.yecheng.log_manager.Data.LogRequestData;


public interface LogMapper {
    void createTable();
    void saveLog(LogData log);

    List<LogData> getLogFromLogId(LogRequestData data);
    List<LogData> getLogFromTime(LogRequestData data);
    List<LogData> getLogFromLogger(LogRequestData data);
    List<LogData> getLogFromPre(LogRequestData data);
    List<LogData> getLogLink(String logId);
    
}
