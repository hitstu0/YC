package com.yecheng.log_manager.Service;

import java.nio.file.Files;
import java.util.List;

import javax.annotation.Resource;

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
            logger.info("begin send log：{}", log);

            LogData logData = new LogData();
            String[] logSplitData = Strings.split(log, '@');
            if (logSplitData == null || logSplitData.length != 2) {
                logger.error("log do not match logId@data");
                continue;
            }

            String logId = logSplitData[0];
            String data = logSplitData[1];

            String[] logIdSplit = Strings.split(logId, ':');
            if (logSplitData == null || logSplitData.length != 2) {
                logger.error("logId do not match time:tag");
                continue;
            }

            logData.setLogIdTime(Long.valueOf(logIdSplit[0]));
            logData.setLogIdTag(logIdSplit[1]);

            logData.setLogTime(System.currentTimeMillis());
            logData.setData(data);

            rmqService.sendLog(logData);
        }

    }

}
