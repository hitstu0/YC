package com.yecheng.nginx_manager.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.aspectj.apache.bcel.classfile.Code;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Data.FlowRouteDefinition;
import com.yecheng.nginx_manager.Data.UpstreamData;
import com.yecheng.nginx_manager.Exception.MyException;
import com.yecheng.nginx_manager.Mybatis.Mapper.FlowRouteMapper;
import com.yecheng.nginx_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class SevenLoadBalanceService {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(SevenLoadBalanceService.class);
    
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;
    
    private static final String APIHost = "apihost";
    private static final String APIPort = "apiport";
    private static final String ServiceWeight = "weight";

    public CodeMsg<String> getSevenLoadBalanceConfig(){
        logger.info("begin get all upstream");
        //获取所有服务名和域名信息
        List<FlowRouteDefinition> services = getAllServiceInfo();
        logger.info("detect service number is:{}", services.size());

        //保存所有生成的动态文件配置
        StringBuilder result = new StringBuilder();
        
        //对每一个服务，生成server块和upstream块
        for (FlowRouteDefinition service : services) {
            String serviceName = service.getService();
            String host = service.getHost();
            logger.info("begin init upstream and server, serviceName is:{}, host is:{}", serviceName, host);
            
            //生成upstream
            List<UpstreamData> upstreamDatas = new LinkedList<>();
            List<ServiceInstance> serviceLists = discoveryClient.getInstances(serviceName);
            
            serviceName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, service.getService());
            for (ServiceInstance instance : serviceLists) {
                UpstreamData uData = new UpstreamData();
                
                Map<String, String> map = instance.getMetadata();
                String ip = map.get(APIHost);
                String port = map.get(APIPort);
                String weight = map.get(ServiceWeight);
                if(ip == null || port == null) {
                   logger.error("service: {} tag do not contain ip or port");
                   throw new MyException(CodeMsg.InfoLack);
                }

                uData.setIp(ip);
                uData.setPort(port);
                uData.setWeight(weight); 

                upstreamDatas.add(uData);
            }

            //根据 upstream信息生成字符串
            String upstreamInfo = getUpstreamFromServiceNameAndData(serviceName, upstreamDatas);
            result.append(upstreamInfo);

            //生成server块
            String serverInfo = getServerBlockFromHostAndServiceName(host, serviceName);
            result.append(serverInfo);
        }
        
        return CodeMsg.SuccessWithData(result.toString());
    }
    
    private String getServerBlockFromHostAndServiceName(String host, String serviceName) {
        StringBuilder builder = new StringBuilder();
        builder.append("server {\n");

        builder.append("listen 80;\n");
        builder.append("server_name " + host + ";\n");

        builder.append("location / {\n");
        builder.append("proxy_pass http://" + serviceName + ";\n");

        builder.append("}\n}\n");

        return builder.toString();
    }

    private String getUpstreamFromServiceNameAndData(String serviceName, List<UpstreamData> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("upstream " + serviceName + " {\n");
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

    private List<FlowRouteDefinition> getAllServiceInfo() {
        SqlSession sqlSession = null;
        try {
           sqlSession = sqlSessionBuilder.getSqlSession();
           FlowRouteMapper mapper = sqlSession.getMapper(FlowRouteMapper.class);
           List<FlowRouteDefinition> definitions = mapper.getAllFlowRouteDefinition();
           
           return definitions;
        } finally {
            if(sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
