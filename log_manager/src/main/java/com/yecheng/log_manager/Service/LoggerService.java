package com.yecheng.log_manager.Service;

import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.tomcat.jni.Time;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yecheng.log_manager.Data.LogData;

@Component
public class LoggerService {
    private Logger logger = LoggerFactory.getLogger(LoggerService.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private RmqService rmqService;

    public void addLogFileCheck(String path) {
        fileService.createFile(path);
    }
    
    public void sendNewLog(String path) {
        List<String> logs = null;
        try {
           logs = fileService.updateFile(path);
        } catch (Exception e) {
           logger.error("get new update log err: {}", e.getMessage());
           return;
        } 
        for (String log : logs) {
            System.out.println(log);
            String[] logSplitData = org.apache.commons.lang3.StringUtils.split(log, " ", 5);
            if (logSplitData == null || logSplitData.length != 5) {
                continue;
            }

            LogData data = null;
            try {
                data = getLogData(logSplitData);
            } catch (Exception e) {
                continue;
            }

            rmqService.sendLog(data);
        }

    }

    private LogData getLogData(String[] datas) throws ParseException {
        LogData logData = new LogData();

        String time = datas[0] + " " + datas[1];
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = fmt.parse(time);
        
        String loggerClass = datas[3];
        String[] loggerSplit = Strings.split(loggerClass, ':');

        String data = datas[4];
        String logId = "";
        String[] dataSplit = org.apache.commons.lang3.StringUtils.split(data, ":", 2);
        if (dataSplit.length == 2) {
            logId = dataSplit[0];
            data = dataSplit[1];
        }

        logData.setLogTime(date.getTime());

        logData.setLevel(datas[2]);

        logData.setLogger(loggerSplit[0]);
        logData.setLine(loggerSplit[1]);
  
        logData.setLogId(logId);
        logData.setData(data);

        return logData;
    }

}
