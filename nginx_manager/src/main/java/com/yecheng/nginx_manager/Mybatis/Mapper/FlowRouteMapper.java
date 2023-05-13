package com.yecheng.nginx_manager.Mybatis.Mapper;

import java.util.List;

import com.yecheng.nginx_manager.Data.FlowRouteDefinition;
import com.yecheng.nginx_manager.Data.FlowRouteDefinitionDB;

public interface FlowRouteMapper {
    //查询
    List<FlowRouteDefinition> getAllFlowRouteDefinition();
    FlowRouteDefinition getFlowRouteDefinitionFromService(int serviceHash);
    
    //删除
    void deleteFlowRouteDefinitionFromService(int serviceHash);

    //插入
    void insertFlowRouteDefinition(FlowRouteDefinitionDB data);

}
