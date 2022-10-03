package com.yecheng.api_manager.Mybatis.SqlSessionFactory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.yecheng.api_manager.Data.RouteDefinitionDBData;
import com.yecheng.api_manager.Data.RouteDefinitionData;
import com.yecheng.ymysql.DataSource.SqlSessionPool;

@Component
public class SqlSessionBuilder implements CommandLineRunner{
    
    @Autowired
    private DiscoveryClient client;
    private SqlSessionPool sqlSessionPool;

    private SqlSessionFactory factory;

    @Override
    public void run(String... args) throws Exception {
        sqlSessionPool = new SqlSessionPool("mysql", "myDatas", "root", "123456yd", client);
        sqlSessionPool.setUnPool(true);
        
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
       sqlSessionPool.addAlias("RouteDefinitionDB", RouteDefinitionDBData.class);
    }
    
    public SqlSession getSqlSession() {
        return factory.openSession();
    }
}
