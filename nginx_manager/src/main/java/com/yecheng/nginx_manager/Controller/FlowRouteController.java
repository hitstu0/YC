package com.yecheng.nginx_manager.Controller;

import java.util.List;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Data.FlowRouteDefinition;
import com.yecheng.nginx_manager.Service.FlowRouteService;

@Controller
@RequestMapping("/flowRoute")
public class FlowRouteController {
    
    @Autowired
    private FlowRouteService flowRouteService;
    
    @GetMapping("/all")
    @ResponseBody
    public CodeMsg<List<FlowRouteDefinition>> getAllFlowRouteDefinition() {
        return CodeMsg.SuccessWithData(flowRouteService.getAll());
    }

    @GetMapping
    @ResponseBody
    public CodeMsg<FlowRouteDefinition> getFromService(@RequestParam(name = "service") String service) {
        return CodeMsg.SuccessWithData(flowRouteService.getFromService(service));
    }

    @PostMapping
    @ResponseBody
    public CodeMsg<String> insertData(@RequestParam(name = "service") String service, @RequestParam(name = "host") String host) {
        flowRouteService.insert(host, service);
        return CodeMsg.Success;
    }

    @DeleteMapping
    @ResponseBody
    public CodeMsg<String> deleteFromService(@RequestParam(name = "service") String service) {
        flowRouteService.deleteFromService(service);
        return CodeMsg.Success;
    }
}
