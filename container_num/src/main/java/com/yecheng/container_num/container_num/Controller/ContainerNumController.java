package com.yecheng.container_num.container_num.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.container_num.container_num.Data.CodeMsg;
import com.yecheng.container_num.container_num.Service.AdjustContainerNumsService;
import com.yecheng.container_num.container_num.Util.Commands;
import com.yecheng.container_num.container_num.Util.ExcInst;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/nums")
public class ContainerNumController {
    Logger logger = LoggerFactory.getLogger(ContainerNumController.class);
    
    @Autowired
    private AdjustContainerNumsService adjustService;
    @GetMapping
    @ResponseBody
    public CodeMsg getContainerNums(@RequestParam(name = "service") String service) {
        logger.info("begin get container nums, service is {}", service);

        String[] command = Commands.GetServiceNums(service);

        return ExcInst.runShell(command);
    }

    @PostMapping
    @ResponseBody
    public CodeMsg AdjustContainerNums(@RequestParam(name = "service") String service, @RequestParam(name = "nums") int nums) {
        logger.info("begin adjust container number, container is:{}, num is:{}", service, nums);
        
        return adjustService.judge(service, nums);
    }
    
}
