package com.yecheng.nginx_manager.Mybatis.SqlSessionFactory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;


import com.yecheng.nginx_manager.Data.FlowRouteDefinition;
import com.yecheng.nginx_manager.Data.FlowRouteDefinitionDB;
import com.yecheng.nginx_manager.Data.RouteDefinitionDBData;
import com.yecheng.nginx_manager.Data.RouteDefinitionData;
import com.yecheng.nginx_manager.Mybatis.Mapper.FlowRouteMapper;
import com.yecheng.nginx_manager.Mybatis.Mapper.RouteDefinitionMapper;
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
        sqlSessionPool.setUnPool(false);
        
        sqlSessionPool.initSqlSessionPool();
        
        addAlias();
        addMapper();

        factory = sqlSessionPool.getSqlSessionFactory();
    }

    private void addMapper() {
        sqlSessionPool.addMapper(FlowRouteMapper.class);
        sqlSessionPool.addMapper(RouteDefinitionMapper.class);
    }

    private void addAlias() {
        sqlSessionPool.addAlias("FlowRouteDefinitionDB", FlowRouteDefinitionDB.class);
        sqlSessionPool.addAlias("FlowRouteDefinition", FlowRouteDefinition.class);
        sqlSessionPool.addAlias("RouteDefinition", RouteDefinitionData.class);
        sqlSessionPool.addAlias("RouteDefinitionDB", RouteDefinitionDBData.class);
    }
    
    public SqlSession getSqlSession() {
        return factory.openSession();
    }
}
