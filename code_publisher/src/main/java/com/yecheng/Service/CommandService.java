package com.yecheng.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandService {
    private static Logger logger = LoggerFactory.getLogger(CommandService.class);
    
    public static boolean doCommand(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream input = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            p.waitFor();
            
            //打印输出信息
            String s = null;
            while ((s = reader.readLine()) != null ) {
                logger.info(s);
            }

            return p.exitValue() == 0;
        } catch (Exception e) {
            logger.error("excute command:{} err, err is:{}", command, e.getMessage());
            return false;
        }
        
    }
}
