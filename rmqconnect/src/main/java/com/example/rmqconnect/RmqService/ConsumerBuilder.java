package com.example.rmqconnect.RmqService;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.example.rmqconnect.Discover.DiscoverService;

public class ConsumerBuilder {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ConsumerBuilder.class);
    
    private DiscoveryClient client;
    private String serviceName;
    

    public ConsumerBuilder(DiscoveryClient client, String serviceName) {
        logger.info("namesrv Name: {}", serviceName);
        this.client = client;
        this.serviceName = serviceName;
    }

    public  DefaultMQPushConsumer getConsumer(String group, String topic, String tag, 
    MessageListenerConcurrently listener) throws MQClientException {
        logger.info("begin init consumer group {}, topic {}, tag{}", group, topic, tag);

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);

        DiscoverService.setDiscoveryClient(client);
        List<ServiceInstance> services = DiscoverService.getServiceList(serviceName);
        if (services == null || services.size() == 0) {
            logger.error("service is null, serviceName is {}", serviceName);
            return null;    
        }
    
        ServiceInstance service = services.get(0);
        String host = service.getHost(); 
        int port = service.getPort();
        
        logger.info("find addr host: {}, port {}", host, port);

        consumer.setNamesrvAddr(host + ":" + port);
        consumer.subscribe(topic, tag);
        consumer.registerMessageListener(listener);
        
        logger.info("init consumer success");
        return consumer;
    }
}
