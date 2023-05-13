package com.yecheng.nginx_manager.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Data.RouteDefinitionData;
import com.yecheng.nginx_manager.Service.RouteDefinitionDataDBService;



@Controller
@RequestMapping("/api_manage")
public class ManagerController {
    private Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private RouteDefinitionDataDBService dbService;
    
    //查询
    @GetMapping
    @ResponseBody
    public CodeMsg<List<RouteDefinitionData>> getAllAPIFromServiceName(
        @RequestParam(name = "service_name") String serviceName ) {
       logger.info("begin get api from service name: {}", serviceName);

       return CodeMsg.SuccessWithData(dbService.getRouteDefinitionDatas(serviceName));
    }
    
    //新增
    @PostMapping
    @ResponseBody
    public CodeMsg<String> addAPI(@RequestParam("service_name") String serviceName, 
        @RequestParam("path") String path ) {
        logger.info("begin add api, service is:{}, path is:{}", serviceName, path);

        return dbService.setRouteDefinitionData(serviceName, path);
    }

    @DeleteMapping
    @ResponseBody
    public CodeMsg<String> deleteAPI(@RequestParam("service_name") String serviceName, 
    @RequestParam("path") String path ) {
        logger.info("begin delete api, service is:{}, path is:{}", serviceName, path);

        return dbService.deleteRouteDefinition(serviceName, path);
    }
}
