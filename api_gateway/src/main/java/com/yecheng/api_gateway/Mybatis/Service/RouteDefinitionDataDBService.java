package com.yecheng.api_gateway.Mybatis.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import com.yecheng.api_gateway.Data.CodeMsg;
import com.yecheng.api_gateway.Data.RouteDefinitionDBData;
import com.yecheng.api_gateway.Data.RouteDefinitionData;
import com.yecheng.api_gateway.Mybatis.Mapper.RouteDefinitionMapper;
import com.yecheng.api_gateway.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class RouteDefinitionDataDBService {
    private Logger logger = LoggerFactory.getLogger(RouteDefinitionDataDBService.class);
    
    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;
    
    public List<RouteDefinitionData> getRouteDefinitionDatas(String serviceName) {
        sqlSessionBuilder.init();
        
        SqlSession sqlSession =  null;
        try {
            //初始化mapper
            sqlSession = sqlSessionBuilder.getSqlSession();
            RouteDefinitionMapper mapper = sqlSession.getMapper(RouteDefinitionMapper.class);
            
            //读取数据库
            int hash = serviceName.hashCode();
            List<RouteDefinitionData> datas = mapper.getRouteDefinitions(hash);
            return datas;
        } catch (Exception e) {
            throw e;
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

    public List<RouteDefinition> changeToRouteDefinition(String serviceName, List<RouteDefinitionData> datas) {
        List<RouteDefinition> definitions = new LinkedList<>();
        for(RouteDefinitionData data : datas) {
            logger.info("begin create RouteDefinition, serviceName:{}, path:{}", serviceName, data.getPath());
            
            RouteDefinition definition = new RouteDefinition();
            
            URI uri = null;
            try {
                uri = new URI("lb://" + serviceName);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (uri == null) return Collections.emptyList();
            
            definition.setUri(uri);
            PredicateDefinition pre = new PredicateDefinition(data.getPath());
            List<PredicateDefinition> pres = new ArrayList<PredicateDefinition>();
            pres.add(pre);
            definition.setPredicates(pres);

            definitions.add(definition);
        }

        return definitions;
    }

}
