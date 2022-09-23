package com.yecheng.ykv.Mybatis.SqlSessionFactory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.yecheng.ykv.Data.Kv;
import com.yecheng.ykv.Mybatis.Mapper.KvMapper;
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
        sqlSessionPool.initSqlSessionPool();
        
        addAlias();
        addMapper();

        factory = sqlSessionPool.getSqlSessionFactory();
    }

    private void addMapper() {
        sqlSessionPool.addMapper(KvMapper.class);
    }

    private void addAlias() {
        sqlSessionPool.addAlias("kv", Kv.class);
    }
    
    public SqlSession getSqlSession() {
        return factory.openSession();
    }
}
