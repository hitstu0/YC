package com.yecheng.yredis.DataSource;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.yecheng.yredis.DiscoverService.DiscoverService;
import com.yecheng.yredis.Util.RedisConfig;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisSessionPool {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JedisSessionPool.class);
    
    private String name;
    private DiscoveryClient discoveryClient;
    private JedisPool jedisPool;
    private RedisConfig config;


    public JedisSessionPool(String name, RedisConfig config, DiscoveryClient discoveryClient) {
        logger.info("begin init redisDataSource, name={}", name);
        this.name = name;
        this.discoveryClient = discoveryClient;
        this.config = config;

        initJedisPool();
    }
    
    public int initJedisPool() {
        DiscoverService discoverService = new DiscoverService(discoveryClient);
        List<ServiceInstance> instances = discoverService.getServiceList(name);
        if (instances == null || instances.size() == 0) {
            logger.error("service:{} not found", name);
            return 0;
        }

        ServiceInstance instance = instances.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        logger.info("get instance host:{}, port:{}", host, port);
        
        createJedisPool(host, port);

        return instances.size();
    }

    private void createJedisPool(String host, int port) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(config.getMaxIdle());
        jedisPoolConfig.setMaxIdle(config.getMaxIdle());
        jedisPoolConfig.setMaxTotal(config.getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(config.getMaxWait());
        jedisPool = new JedisPool(jedisPoolConfig, host, port);   
    }

    public JedisPool GetJedisPool() {
        return jedisPool;
    }
}
