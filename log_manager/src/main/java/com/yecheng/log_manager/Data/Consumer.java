package com.yecheng.log_manager.Data;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.example.rmqconnect.RmqService.ConsumerBuilder;
import com.yecheng.log_manager.Service.LogDBService;

@Component
public class Consumer {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);
    
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LogDBService logDBService;

    @Bean(name = "rmqconsumer")
    public DefaultMQPushConsumer getConsumer() throws MQClientException {
        logger.info("start init consumer");
        ConsumerBuilder builder = new ConsumerBuilder(discoveryClient, "rocketmq-9876");

        try {
            DefaultMQPushConsumer consumer = builder.getConsumer("yecheng", "logs", "send", new Listener());
            consumer.start();

            logger.info("init consumer success");
            
            return consumer;
        } catch (MQClientException e) {
            logger.error("create consumer err: {}", e.getMessage());
            throw e;
        }

        
    }

    class Listener implements MessageListenerConcurrently {

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt msg : msgs) {
                LogData logData = JSON.toJavaObject(JSON.parseObject(new String(msg.getBody())), LogData.class) ;
                logDBService.writeLog(logData);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        
    }
}
