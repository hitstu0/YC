package com.yecheng.log_manager.Util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner{
    
    @Autowired
    private FileListener listener;

    @Override
    public void run(String... args) throws Exception {
        //监控目录
        String rootDir = "/Users/bytedance/test";
    
        //轮询间隔
        long interval = TimeUnit.SECONDS.toMillis(1);

        IOFileFilter directory = FileFilterUtils.and(
            FileFilterUtils.directoryFileFilter(),
            HiddenFileFilter.VISIBLE
        );

        IOFileFilter files = FileFilterUtils.and(
            FileFilterUtils.fileFileFilter(),
            FileFilterUtils.suffixFileFilter(".log")
        );

        IOFileFilter filter = FileFilterUtils.or(directory, files);

        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
        observer.addListener(listener);

        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        
        monitor.start();
    }
    
}
