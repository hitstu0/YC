package com.yecheng.api_gateway.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

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
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.yecheng.api_gateway.Data.RouteDefinitionData;
import com.yecheng.api_gateway.Mybatis.Mapper.RouteDefinitionMapper;
import com.yecheng.api_gateway.Mybatis.Service.RouteDefinitionDataDBService;
import com.yecheng.api_gateway.Mybatis.SqlSessionFactory.SqlSessionBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {
    private static Logger logger = LoggerFactory.getLogger(MysqlRouteDefinitionRepository.class);
    
    //网关对应服务
    @Value("${service_name}")
    private String serviceName;
    //该服务的路由路径
    private List<RouteDefinition> routeDefinitionList = new LinkedList<>();
    
    //工具类
    @Autowired
    private RouteDefinitionDataDBService dbService;

    private ApplicationEventPublisher publisher;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routeDefinitionList);
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

    //加载 mysql 中的配置
    @PostConstruct
    public void init() {
        load();
    }

    public void load() {
        logger.info("begin getRouteDefinition From mysql, service name is: {}", serviceName);
        
        List<RouteDefinitionData> datas = dbService.getRouteDefinitionDatas(serviceName);
        routeDefinitionList = dbService.changeToRouteDefinition(serviceName, datas);

    }
}
