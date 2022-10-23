package com.yecheng.log_manager.Util;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yecheng.log_manager.Service.LoggerService;

@Component
public class FileListener extends FileAlterationListenerAdaptor{
    private Logger logger = LoggerFactory.getLogger(FileListener.class);
    
    @Autowired
    private LoggerService loggerService;

    @Override
    public void onFileCreate(File file) {
        loggerService.addLogFileCheck(file.getAbsolutePath());
    }

    @Override
    public void onFileChange(File file) {
        loggerService.sendNewLog(file.getAbsolutePath());
    }
}
