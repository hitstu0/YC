package com.yecheng.nginx_manager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.nginx_manager.Data.CodeMsg;
import com.yecheng.nginx_manager.Service.FourLoadBalanceService;
import com.yecheng.nginx_manager.Service.SevenLoadBalanceService;

@Controller
@RequestMapping("/nginx_config")
public class NginxConfigController {
    
    @Autowired
    private SevenLoadBalanceService seven;

    @Autowired
    private FourLoadBalanceService four;
    
    @GetMapping("/seven")
    @ResponseBody
    public CodeMsg<String> getSevenLoadBalance() {
        return seven.getSevenLoadBalanceConfig();
    }
    
    @GetMapping("/four")
    @ResponseBody
    public CodeMsg<String> getFourLoadBalance() {
        return four.getFourLoadBalanceConfig();
    }
}
