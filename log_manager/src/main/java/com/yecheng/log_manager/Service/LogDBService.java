package com.yecheng.log_manager.Service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yecheng.log_manager.Data.LogData;
import com.yecheng.log_manager.Mybatis.Mapper.LogMapper;
import com.yecheng.log_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class LogDBService {
    
    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;

    public void writeLog(LogData logData) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
            logMapper.saveLog(logData);

            sqlSession.commit();
        } finally {
            if(sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
