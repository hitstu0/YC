package com.yecheng.nginx_manager.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.aspectj.apache.bcel.classfile.Code;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Data.FlowRouteDefinition;
import com.yecheng.nginx_manager.Data.UpstreamData;
import com.yecheng.nginx_manager.Exception.MyException;
import com.yecheng.nginx_manager.Mybatis.Mapper.FlowRouteMapper;
import com.yecheng.nginx_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class NginxConfigService {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(NginxConfigService.class);
    
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;
    
    private static final String APIHost = "ApiHost";
    private static final String APIPort = "ApiPort";
    private static final String ServiceWeight = "ServiceWeight";

    public CodeMsg<String> getAllUpstream(){
        logger.info("begin get all upstream");
        //获取所有服务名
        List<String> services = getAllServiceName();

        //保存所有upstream字符串
        StringBuilder result = new StringBuilder();

        for (String serviceName : services) {
            logger.info("begin init upstream, serviceName is:{}", serviceName);
            //保存该服务所有upstream实例
            List<UpstreamData> upstreamDatas = new LinkedList<>();

            List<ServiceInstance> serviceLists = discoveryClient.getInstances(serviceName);
            for (ServiceInstance instance : serviceLists) {
                UpstreamData uData = new UpstreamData();
                
                Map<String, String> map = instance.getMetadata();
                String ip = map.get(APIHost);
                String port = map.get(APIPort);
                String weight = map.get(ServiceWeight);
                if(ip == null || port == null) {
                   throw new MyException(CodeMsg.InfoLack);
                }

                uData.setIp(ip);
                uData.setPort(port);
                uData.setWeight(weight);

                upstreamDatas.add(uData);
            }

            //根据 upstream信息生成字符串
            String info = getUpstreamFromServiceNameAndData(serviceName, upstreamDatas);
            result.append(info);
        }

        return CodeMsg.SuccessWithData(result.toString());
    }
    
    private String getUpstreamFromServiceNameAndData(String serviceName, List<UpstreamData> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("upstream " + serviceName + "{\n");
        for(UpstreamData data : list) {
            builder.append("server " + data.getIp() + ":" + data.getPort());
            if (data.getWeight() != null) {
                builder.append(" weight=" + data.getWeight());
            }
            builder.append(";\n");
        }

        builder.append("}\n");
        return builder.toString();
    }

    private List<String> getAllServiceName() {
        SqlSession sqlSession = null;
        try {
           sqlSession = sqlSessionBuilder.getSqlSession();
           FlowRouteMapper mapper = sqlSession.getMapper(FlowRouteMapper.class);
           List<FlowRouteDefinition> definitions = mapper.getAllFlowRouteDefinition();
           
           List<String> serviceNames = new LinkedList<>();
           for(FlowRouteDefinition definition : definitions) {
               serviceNames.add(definition.getService());
           }
           
           return serviceNames;
        } finally {
            if(sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
