package com.yecheng.api_gateway.Mybatis.SqlSessionFactory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.yecheng.api_gateway.Data.RouteDefinitionData;
import com.yecheng.ymysql.DataSource.SqlSessionPool;

@Component
public class SqlSessionBuilder implements CommandLineRunner{
    private Logger logger = org.slf4j.LoggerFactory.getLogger(SqlSessionBuilder.class);

    @Autowired
    private DiscoveryClient client;
    private SqlSessionPool sqlSessionPool;

    private SqlSessionFactory factory;

    @Override
    public void run(String... args) throws Exception {
        logger.info("begin init mysql source");
        sqlSessionPool = new SqlSessionPool("mysql", "myDatas", "root", "123456yd", client);
        sqlSessionPool.initSqlSessionPool();
        
        addAlias();
        addMapper();

        factory = sqlSessionPool.getSqlSessionFactory();
    }

    private void addMapper() {
        sqlSessionPool.addMapper(RouteDefinitionData.class);
    }

    private void addAlias() {
       sqlSessionPool.addAlias("RouteDefinition", RouteDefinitionData.class);
    }
    
    public SqlSession getSqlSession() {
        return factory.openSession();
    }
}
