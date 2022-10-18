package com.yecheng.api_gateway.Mybatis.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yecheng.api_gateway.Data.RouteDefinitionDBData;
import com.yecheng.api_gateway.Data.RouteDefinitionData;

public interface RouteDefinitionMapper {
    List<RouteDefinitionData> getRouteDefinitions(int serviceHash);
}
