package com.yecheng.nginx_manager.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.yecheng.nginx_manager.Data.CodeMsg;

@Service
public class FourLoadBalanceService {
    private Logger logger = LoggerFactory.getLogger(FourLoadBalanceService.class);
    
    @Autowired
    private DiscoveryClient discoveryClient;

    private static final String Nginx = "nginx";
    
    public CodeMsg<String> getFourLoadBalanceConfig() {
        logger.info("beginning get four load balance configuration");
         
        //服务发现找到所有nginx实例
        List<ServiceInstance> serviceLists = discoveryClient.getInstances(Nginx);
        logger.info("get nginx instance number: {}", serviceLists.size());
        
        //生成配置文件
        StringBuilder upstream = new StringBuilder();
        for (ServiceInstance service : serviceLists) {
            String ip = service.getHost();
            int port = service.getPort();

            Map<String, String> meta = service.getMetadata();
            String weight = meta.get("weight");

            upstream.append("server " + ip + ":" + port);
            if (weight != null) {
                upstream.append(" weight=" + weight);
            }
            upstream.append(";\n");
        }

        StringBuilder result = new StringBuilder();
        result.append("stream {\n");
        result.append("upstream nginx {\n");
        result.append(upstream.toString());
        result.append("}\nserver {\nlisten 81;\n proxy_pass nginx; proxy_set_header log-id $request_id;\n}\n}\n");

        return CodeMsg.SuccessWithData(result.toString());
    }
}
