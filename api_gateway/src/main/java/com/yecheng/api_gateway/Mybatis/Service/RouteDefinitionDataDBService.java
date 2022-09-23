package com.yecheng.api_gateway.Mybatis.Service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yecheng.api_gateway.Data.RouteDefinitionData;
import com.yecheng.api_gateway.Mybatis.Mapper.RouteDefinitionMapper;
import com.yecheng.api_gateway.Mybatis.SqlSessionFactory.SqlSessionBuilder;

@Service
public class RouteDefinitionDataDBService {
    
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
}
