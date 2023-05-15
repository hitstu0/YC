package com.yecheng.log_manager.Mybatis.SqlSessionFactory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.yecheng.log_manager.Data.LogData;
import com.yecheng.log_manager.Data.LogRequestData;
import com.yecheng.log_manager.Mybatis.Mapper.LogMapper;
import com.yecheng.ymysql.DataSource.SqlSessionPool;


@Component
public class SqlSessionBuilder implements CommandLineRunner{
    private Logger logger = LoggerFactory.getLogger(SqlSessionBuilder.class);

    @Autowired
    private DiscoveryClient client;
    private SqlSessionPool sqlSessionPool;

    private SqlSessionFactory factory;

    @Override
    public void run(String... args) throws Exception {
        logger.info("sqlsession start");
        sqlSessionPool = new SqlSessionPool("mysql-3306", "myDatas", "root", "123456yd", client);
        sqlSessionPool.initSqlSessionPool();
        
        addAlias();
        addMapper();

        factory = sqlSessionPool.getSqlSessionFactory();
        try {
            SqlSession sqlSession = getSqlSession();
            LogMapper logMapper = sqlSession.getMapper(LogMapper.class);
            logMapper.createTable();
            sqlSession.commit();
            sqlSession.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addMapper() {
        sqlSessionPool.addMapper(LogMapper.class);
    }

    private void addAlias() {
        sqlSessionPool.addAlias("log", LogData.class);
        sqlSessionPool.addAlias("req", LogRequestData.class);
    }
    
    public SqlSession getSqlSession() {
        return factory.openSession();
    }
}
