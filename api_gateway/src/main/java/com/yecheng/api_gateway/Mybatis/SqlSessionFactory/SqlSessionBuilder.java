package com.yecheng.api_gateway.Mybatis.SqlSessionFactory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.yecheng.api_gateway.Data.RouteDefinitionDBData;
import com.yecheng.api_gateway.Data.RouteDefinitionData;
import com.yecheng.api_gateway.Mybatis.Mapper.RouteDefinitionMapper;
import com.yecheng.ymysql.DataSource.SqlSessionPool;

@Component
public class SqlSessionBuilder {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(SqlSessionBuilder.class);

    @Autowired
    private DiscoveryClient client;

    private SqlSessionPool sqlSessionPool;

    private SqlSessionFactory factory;


    public void init() {
        if(factory != null) {
            return;
        }
        logger.info("begin init mysql source");
        sqlSessionPool = new SqlSessionPool("mysql", "myDatas", "root", "123456yd", client);
        sqlSessionPool.initSqlSessionPool();
        
        addAlias(); 
        addMapper();

        factory = sqlSessionPool.getSqlSessionFactory();

        try {
            SqlSession sqlSession = getSqlSession();
            RouteDefinitionMapper mapper = sqlSession.getMapper(RouteDefinitionMapper.class);
            mapper.createTable();
            sqlSession.commit();
            sqlSession.close();
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void addMapper() {
        sqlSessionPool.addMapper(RouteDefinitionMapper.class);
    }

    private void addAlias() {
        sqlSessionPool.addAlias("RouteDefinition", RouteDefinitionData.class);
        sqlSessionPool.addAlias("RouteDefinitionDB", RouteDefinitionDBData.class);
    }
    
    public SqlSession getSqlSession() {
        return factory.openSession();
    }
}
