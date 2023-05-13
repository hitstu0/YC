package com.yecheng.log_manager.Service;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yecheng.log_manager.Data.LogData;
import com.yecheng.log_manager.Mybatis.Mapper.LogMapper;
import com.yecheng.log_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class LogDBService {
    Logger logger = LoggerFactory.getLogger(LogDBService.class);

    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;

    public void writeLog(LogData logData) {
        
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
            logMapper.saveLog(logData);
            sqlSession.commit();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
