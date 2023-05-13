package com.yecheng.api_gateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.api_gateway.Data.CodeMsg;
import com.yecheng.api_gateway.Service.MysqlRouteDefinitionRepository;

@Controller
@RequestMapping("/refresh")
public class DynamicRouteController {
    
    @Autowired
    private MysqlRouteDefinitionRepository repository;
    
    @GetMapping
    @ResponseBody
    public CodeMsg refreshRouteDefinition() {
        repository.load();
        repository.refreshRoutes();

        return CodeMsg.Success;
    }
}
