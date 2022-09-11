package com.yecheng.ykv.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.yecheng.yredis.DataSource.JedisSessionPool;
import com.yecheng.yredis.Util.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisBuilder implements CommandLineRunner{
    
    @Autowired
    private DiscoveryClient discoveryClient;

    private JedisPool jedisPool;

    @Override
    public void run(String... args) throws Exception {
        RedisConfig config = new RedisConfig();
        config.setMaxActive(1000);
        config.setMaxIdle(10000);
        config.setMaxWait(10000);
        config.setTimeOut(10000);
        
        JedisSessionPool jedisSessionPool = new JedisSessionPool("redis", config, discoveryClient);
        jedisPool = jedisSessionPool.GetJedisPool();
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }
    
}
