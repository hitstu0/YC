package com.yecheng.api_gateway.Mybatis.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yecheng.api_gateway.Data.RouteDefinitionDBData;
import com.yecheng.api_gateway.Data.RouteDefinitionData;

public interface RouteDefinitionMapper {
    void createTable();
    List<RouteDefinitionData> getRouteDefinitionsByService(int serviceHash);
    List<RouteDefinitionData> getAllRouteDefinitions();
    void setRouteDefinition(RouteDefinitionDBData dbData);
    void deleteRouteDefinition(@Param("pathHash") int pathHash, @Param("serviceHash") int serviceHash);
}
