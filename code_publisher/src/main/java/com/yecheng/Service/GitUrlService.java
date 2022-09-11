package com.yecheng.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.yecheng.Data.KvResponse;

public class GitUrlService {
    private Logger logger = LoggerFactory.getLogger(GitUrlService.class);
    
    private static final String KV_URL = "http://120.77.221.92:8080/kv";
    private String projectName;

    public GitUrlService(String projectName) {
        this.projectName = projectName;
    }

    public String getGitURL() {
        HttpURLConnection connection = null;
        String requestURL = KV_URL + "/?key=" + projectName; 
        logger.info("request url is:{}", requestURL);

        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", "token=123456:testa");
            connection.connect();

            int responseCode = connection.getResponseCode();
            logger.info("response code is:{}", responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                StringBuilder resultBuffer = new StringBuilder();
                String line;
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                
                String resultJson = resultBuffer.toString();
                logger.info("response body is:{}", resultJson);

                KvResponse response = JSON.toJavaObject(JSON.parseObject(resultJson), KvResponse.class);
                return response.getMsg() == null ? "" : response.getMsg().getValue();
            } else {
                //输出错误信息
                InputStream inputStream = connection.getErrorStream();
                StringBuilder resultBuffer = new StringBuilder();
                String line;
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                
                String result = resultBuffer.toString();

                logger.error("err is:{}", result);
                return "";
            } 
        } catch (Exception e) {
            logger.error("request err, url is {}, err is {}", requestURL, e.getMessage());
            return "";
        }
    }
}
