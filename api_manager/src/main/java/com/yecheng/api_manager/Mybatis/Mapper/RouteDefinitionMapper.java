package com.yecheng.api_manager.Mybatis.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yecheng.api_manager.Data.RouteDefinitionDBData;
import com.yecheng.api_manager.Data.RouteDefinitionData;


public interface RouteDefinitionMapper {
    List<RouteDefinitionData> getRouteDefinitions(int serviceHash);

    void setRouteDefinition(RouteDefinitionDBData dbData);

    void deleteRouteDefinition(@Param("pathHash") int pathHash, @Param("serviceHash") int serviceHash);
}
