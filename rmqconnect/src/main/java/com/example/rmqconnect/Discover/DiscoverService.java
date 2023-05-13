package com.example.rmqconnect.Discover;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class DiscoverService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DiscoverService.class);
    
    private static DiscoveryClient discoveryClient;

    public static List<ServiceInstance> getServiceList(String name) {
        logger.info("discoveryClient: {}",discoveryClient);
        return discoveryClient.getInstances(name);
    }

    public static void setDiscoveryClient(DiscoveryClient client) {
        discoveryClient = client;
    }
}