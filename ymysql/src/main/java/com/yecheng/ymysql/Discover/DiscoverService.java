package com.yecheng.ymysql.Discover;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class DiscoverService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DiscoverService.class);

    private DiscoveryClient discoveryClient;

    public DiscoverService(DiscoveryClient client) {
        this.discoveryClient = client;
    }

    public List<ServiceInstance> getServiceList(String name) {
        logger.info("begin get service list, name is {}", name);
        
        List<ServiceInstance> instances = discoveryClient.getInstances(name);
        logger.info("get service: {} number is {}", name, instances.size());
         
        return instances;
    }
}
