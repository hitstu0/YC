package com.yecheng.nginx_manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Service.NginxConfigService;

@Controller
@RequestMapping("/nginx_config")
public class NginxConfigController {
    
    @Autowired
    private NginxConfigService service;
    
    @GetMapping("/dynamic")
    @ResponseBody
    public CodeMsg<String> getAllUpstream() {
        return service.getAllDynamicConfig();
    }
    
}
