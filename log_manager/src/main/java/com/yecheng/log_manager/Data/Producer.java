package com.yecheng.log_manager.Data;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.rmqconnect.RmqService.ProducerBuilder;

@Component
public class Producer {

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Bean(name = "producer")
    public DefaultMQProducer getProducer() throws MQClientException {
        ProducerBuilder builder = new ProducerBuilder(discoveryClient, "namesrv");
        DefaultMQProducer producer =  builder.getProducer("yecheng", 100);
        producer.start();
        return producer;
    }
}
