package com.yecheng.api_gateway.Service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.yecheng.api_gateway.Data.RouteDefinitionData;
import com.yecheng.api_gateway.Mybatis.Mapper.RouteDefinitionMapper;
import com.yecheng.api_gateway.Mybatis.Service.RouteDefinitionDataDBService;
import com.yecheng.api_gateway.Mybatis.SqlSessionFactory.SqlSessionBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware{
    private static Logger logger = LoggerFactory.getLogger(MysqlRouteDefinitionRepository.class);
     
    @Value("${service_name}")
    private String serviceName;
    
    @Autowired
    private RouteDefinitionDataDBService dbService;

    private ApplicationEventPublisher publisher;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        logger.info("begin getRouteDefinitionFrom mysql, service name is: {}", serviceName);
        
        List<RouteDefinitionData> datas = dbService.getRouteDefinitionDatas(serviceName);
        List<RouteDefinition> definitions = changeToRouteDefinition(datas);

        return Flux.fromIterable(definitions);

    } 
    
    private List<RouteDefinition> changeToRouteDefinition( List<RouteDefinitionData> datas) {
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
    

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void refreshRoutes() {
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
