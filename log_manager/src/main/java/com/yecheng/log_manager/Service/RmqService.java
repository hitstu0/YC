package com.yecheng.log_manager.Service;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RmqService {
    Logger logger = LoggerFactory.getLogger(RmqService.class);
    
    @Autowired
    private DefaultMQProducer producer;

    public boolean sendLog(String log) {
        Message msg = new Message();
        msg.setTopic("logs");
        msg.setTags("send");
        msg.setBody(log.getBytes());
        try {
            producer.send(msg);
        } catch (Exception e) {
            logger.error("send log err: {}", e.getMessage());
            return false;
        } 

        return true;
    }
}
