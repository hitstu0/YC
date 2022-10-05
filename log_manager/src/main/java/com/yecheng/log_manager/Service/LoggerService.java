package com.yecheng.log_manager.Service;

import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            logger.info("begin send log");
            rmqService.sendLog(log);
        }

    }

}
