package com.yecheng.ymysql.DataSource;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.slf4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.yecheng.ymysql.Discover.DiscoverService;

public class SqlSessionPool {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SqlSessionPool.class);

    //mysql信息
    private String serviceName;
    private String dataBase;
    private String userName;
    private String password;
    private DiscoveryClient client;
    private boolean unpool;

    //连接信息
    private final String driver = "com.mysql.jdbc.Driver";
    private final String urlPre = "jdbc:mysql://";
    private final String urlPost = "?useUnicode=true&characterEncoding=utf8&useSSL=false" + 
    "&allowPublicKeyRetrieval=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true" +
    "&autoReconnect=true&autoReconnectForPools=true";

    private DataSource dataSource;
    private Configuration configuration;



    public SqlSessionPool(String service, String dataBase, String user, String pass,DiscoveryClient client) {
        logger.info("begin init sqlSessionPool, serviceName:{}, dataBase:{}, userName:{}",
        serviceName, dataBase, userName);

        this.serviceName = service;
        this.dataBase = dataBase;
        this.userName = user;
        this.password = pass;
        this.client = client;
    }
    
    public void setUnPool(boolean unpool) {
        this.unpool = unpool;
    }

    public int initSqlSessionPool() {
        DiscoverService discoverService = new DiscoverService(client);
        List<ServiceInstance> instances = discoverService.getServiceList(serviceName);
        if (instances == null || instances.size() == 0) {
            logger.error("service:{} not found", serviceName);
            return 0;
        }
        logger.info("found service number is {}, serviceName:{}", instances.size(), serviceName);
        
        ServiceInstance instance = instances.get(0);
        String host = instance.getHost();
        int port = instance.getPort();

        logger.info("choose service host:{}, port:{}", host, port);

        initDataSource(host, port);

        return instances.size();
    }
    
    public void addMapper(Class<?> clazz) {
        configuration.addMapper(clazz);
    }
    
    public void addAlias(String alias, Class<?> clazz) {
        TypeAliasRegistry registry = configuration.getTypeAliasRegistry();
        registry.registerAlias(alias, clazz);
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    private void initDataSource(String host, int port) {
        String url = urlPre + host + ":" + port + "/" + dataBase + urlPost;
        logger.info("datasource url is: {}", url);
        
        if (!unpool) dataSource = new PooledDataSource(driver, url, userName, password);
        else dataSource = new UnpooledDataSource(driver, url, userName, password);
        try {
            dataSource.setLoginTimeout(1800);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //事物
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        //环境
        Environment environment = new Environment("development", transactionFactory, dataSource);
        //配置
        configuration = new Configuration(environment);
        configuration.setMapUnderscoreToCamelCase(true);
    }

}
