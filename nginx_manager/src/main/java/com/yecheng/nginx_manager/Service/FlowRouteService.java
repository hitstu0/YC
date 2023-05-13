package com.yecheng.nginx_manager.Service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Data.FlowRouteDefinition;
import com.yecheng.nginx_manager.Data.FlowRouteDefinitionDB;
import com.yecheng.nginx_manager.Mybatis.Mapper.FlowRouteMapper;
import com.yecheng.nginx_manager.Mybatis.SqlSessionFactory.SqlSessionBuilder;

import ch.qos.logback.classic.db.DBAppender;




@Service
public class FlowRouteService {
    
    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;
    
    public List<FlowRouteDefinition> getAll() {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            FlowRouteMapper mapper = sqlSession.getMapper(FlowRouteMapper.class);
            return mapper.getAllFlowRouteDefinition();
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

    public FlowRouteDefinition getFromService(String service) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            FlowRouteMapper mapper = sqlSession.getMapper(FlowRouteMapper.class);
            return mapper.getFlowRouteDefinitionFromService(service.hashCode());
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

    public void deleteFromService(String service) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            FlowRouteMapper mapper = sqlSession.getMapper(FlowRouteMapper.class);
            mapper.deleteFlowRouteDefinitionFromService(service.hashCode());
            sqlSession.commit();
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }

    public void insert(String host, String service) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            FlowRouteMapper mapper = sqlSession.getMapper(FlowRouteMapper.class);
            FlowRouteDefinitionDB db = new FlowRouteDefinitionDB();
            
            db.setHost(host);
            db.setService(service);
            db.setServiceHash(service.hashCode());
            
            mapper.insertFlowRouteDefinition(db);
            sqlSession.commit();
        } finally {
            if (sqlSession != null) sqlSession.close();
        }
    }
}
