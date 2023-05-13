package com.example.rmqconnect.RmqService;

import java.util.List;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.example.rmqconnect.Discover.DiscoverService;

public class ProducerBuilder {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProducerBuilder.class);
    
    private DiscoveryClient client;
    private String serviceName;
    public ProducerBuilder(DiscoveryClient client, String serviceName) {
        logger.info("serviceName is {}", serviceName);
        this.client = client;
        this.serviceName = serviceName;
    }

    public  DefaultMQProducer getProducer(String group, int timeout) {
        logger.info("begin init producer, group {}, timeout {}", group, timeout);
        DefaultMQProducer producer = new DefaultMQProducer(group);

        DiscoverService.setDiscoveryClient(client);
        List<ServiceInstance> services = DiscoverService.getServiceList(serviceName);
        if (services == null || services.size() == 0) {
            logger.error("service is null, serviceName is {}", serviceName);
            return null;    
        }

        ServiceInstance service = services.get(0);
        String host = service.getHost();
        int port = service.getPort();
        logger.info("find addr {}, port {}", host, port);

        producer.setNamesrvAddr(host + ":" + port);
        producer.setSendMsgTimeout(timeout);
        
        logger.info("init producer success");
        return producer;
    }
}
