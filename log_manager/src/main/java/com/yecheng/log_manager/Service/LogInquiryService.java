package com.yecheng.log_manager.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yecheng.log_manager.Data.LogData;
import com.yecheng.log_manager.Data.LogRequestData;
import com.yecheng.log_manager.Mybatis.Mapper.LogMapper;
import com.yecheng.log_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class LogInquiryService {
    Logger logger = LoggerFactory.getLogger(LogInquiryService.class);
    
    @Autowired
    private SqlSessionBuilder builder;

    public List<LogData> getLog(LogRequestData data) {
        SqlSession sqlSession = null;
        try {
           sqlSession = builder.getSqlSession();
           LogMapper mapper = sqlSession.getMapper(LogMapper.class);
           List<LogData> result = null;
           if(data.isUsePre()) {
             result = mapper.getLogFromLoggerAndPre(data);
           } else {
             result = mapper.getLogFromLogger(data);
           }

           return result;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public String spliceLog(List<LogData> logs) {
        if(logs == null) {
            return "must add scope";
        }

        StringBuilder result = new StringBuilder();
        DateFormat dateFormat = DateFormat.getDateInstance();
        for (LogData logData : logs) {
            Date date = new Date();
            date.setTime(logData.getLogTime());
            String time = dateFormat.format(date);

            String log = String.format("%s %s %s:%s %s %s", time, logData.getLevel(), logData.getLogger(), logData.getLine(),
            logData.getLogId(), logData.getData());
           
            result.append(log + "\n");
        }
        return result.toString();
    }

    public List<LogData> getLogLink(String logId) {
        SqlSession sqlSession = null;
        try {
           sqlSession = builder.getSqlSession();
           LogMapper mapper = sqlSession.getMapper(LogMapper.class);
           List<LogData> link = mapper.getLogLink(logId);
           return link;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}


