package com.yecheng.log_manager.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileService {
    private Logger logger = LoggerFactory.getLogger(FileService.class);

    //记录每个文件读取的位置
    private Map<String, Long> indexMap = new ConcurrentHashMap<>();

    //创建文件新增读取位置
    public void createFile(String path) {
        indexMap.put(path, new Long(0));
    }
 
    //删除文件删除读取位置
    public void deleteFile(String path) {
         indexMap.remove(path);
    }

    //更改文件则读取日志内容
    public List<String> updateFile(String path) throws IOException {
        List<String> list = new LinkedList<>();
       
        File file = new File(path);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        
        Long index = indexMap.get(path);
        if( index == null ) {
            index = new Long(0);
        }

        randomAccessFile.seek(index);
        
        String log = randomAccessFile.readLine();
        for(; log != null ; log = randomAccessFile.readLine()) {
            list.add(log);
        }

        indexMap.put(path, randomAccessFile.length());
        return list;
    }
}
