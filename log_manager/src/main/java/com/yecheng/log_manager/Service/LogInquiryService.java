package com.yecheng.log_manager.Service;

import java.text.DateFormat;
import java.util.Comparator;
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

           //没指定Logger则判断是否使用前缀扫描
           if(data.getLogger().equals("")) {
              if (data.isUsePre()) {
                return mapper.getLogFromPre(data);
              } else {
                return null;
              }
           } else {
              //使用LogId
              if (data.getLogId() != "" ) {
                return mapper.getLogFromLogId(data);
              }

              //使用时间
              if (data.getBeginTime() != 0 && data.getEndTime() != 0) {
                return mapper.getLogFromTime(data);
              }

              //使用logger
              return mapper.getLogFromLogger(data);
           }
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
           link.sort(new LogLinkComparator());

           return link;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    class LogLinkComparator implements Comparator<LogData> {

        @Override
        public int compare(LogData o1, LogData o2) {
            return (int)(o1.getLogTime() - o2.getLogTime());
        }
    }
}


