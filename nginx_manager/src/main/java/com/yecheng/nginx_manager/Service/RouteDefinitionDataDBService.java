package com.yecheng.nginx_manager.Service;

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

import org.springframework.stereotype.Service;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Data.RouteDefinitionDBData;
import com.yecheng.nginx_manager.Data.RouteDefinitionData;
import com.yecheng.nginx_manager.Mybatis.Mapper.RouteDefinitionMapper;
import com.yecheng.nginx_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;




@Service
public class RouteDefinitionDataDBService {
    private Logger logger = LoggerFactory.getLogger(RouteDefinitionDataDBService.class);
    
    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;
    
    public List<RouteDefinitionData> getRouteDefinitionDatas(String serviceName) {       
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

    public CodeMsg<String> setRouteDefinitionData(String serviceName, String path) {
        RouteDefinitionDBData dbData = new RouteDefinitionDBData();
        dbData.setServiceName(serviceName);
        dbData.setPath(path);
        dbData.setServiceHash(serviceName.hashCode());
        dbData.setPathHash(path.hashCode());

        SqlSession sqlSession = null;
        try {
           sqlSession = sqlSessionBuilder.getSqlSession();
           RouteDefinitionMapper mapper = sqlSession.getMapper(RouteDefinitionMapper.class);

           mapper.setRouteDefinition(dbData);
        } finally {
           if(sqlSession != null) {
               sqlSession.close();
           }
        }
        return CodeMsg.Success;
   }

   public CodeMsg<String> deleteRouteDefinition(String serviceName, String path) {
       SqlSession sqlSession = null;
        try {
           sqlSession = sqlSessionBuilder.getSqlSession();
           RouteDefinitionMapper mapper = sqlSession.getMapper(RouteDefinitionMapper.class);

           mapper.deleteRouteDefinition(path.hashCode(), serviceName.hashCode());
        } finally {
           if(sqlSession != null) {
               sqlSession.close();
           }
        }
        return CodeMsg.Success;
   }
}
