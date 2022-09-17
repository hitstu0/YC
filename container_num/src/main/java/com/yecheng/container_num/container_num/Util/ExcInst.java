package com.yecheng.container_num.container_num.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yecheng.container_num.container_num.Data.CodeMsg;

public class ExcInst {
    private static Logger logger = LoggerFactory.getLogger(ExcInst.class);

    public static CodeMsg runShell(String[] command) {
         logger.info(command[2]);

         try {
            Process p = Runtime.getRuntime().exec(command);
            int exitValue = p.waitFor();

            if (exitValue == 0) return CodeMsg.SuccessWithData( getOutPut( p.getInputStream()).trim());
            else return CodeMsg.FailWithData( getOutPut( p.getErrorStream()));

        } catch (Exception e) {
            logger.error("run shell execption:{}", e.getMessage());
            return CodeMsg.InterException;
        }

    }

    private static String getOutPut(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            logger.info("output is:{}", builder.toString());
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
