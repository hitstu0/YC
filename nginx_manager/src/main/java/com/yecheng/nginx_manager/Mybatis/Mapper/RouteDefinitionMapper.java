package com.yecheng.nginx_manager.Mybatis.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yecheng.nginx_manager.Data.RouteDefinitionDBData;
import com.yecheng.nginx_manager.Data.RouteDefinitionData;



public interface RouteDefinitionMapper {
    List<RouteDefinitionData> getRouteDefinitions(int serviceHash);

    void setRouteDefinition(RouteDefinitionDBData dbData);

    void deleteRouteDefinition(@Param("pathHash") int pathHash, @Param("serviceHash")int serviceHash);
}
