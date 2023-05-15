package com.yecheng.log_manager.Data;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.rmqconnect.RmqService.ProducerBuilder;

@Component
public class Producer {
    private Logger logger = LoggerFactory.getLogger(Producer.class);
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Bean(name = "rmqproducer")
    public DefaultMQProducer getProducer() throws MQClientException {
        logger.info("start init producer");
        ProducerBuilder builder = new ProducerBuilder(discoveryClient, "rocketmq-9876");
        DefaultMQProducer producer =  builder.getProducer("yecheng", 300);
        
        producer.start();

        logger.info("init producer success");
        return producer;
    }
}
