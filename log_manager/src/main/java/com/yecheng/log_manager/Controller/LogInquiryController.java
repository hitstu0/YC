package com.yecheng.log_manager.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.log_manager.Data.LogData;
import com.yecheng.log_manager.Data.LogRequestData;
import com.yecheng.log_manager.Service.LogInquiryService;

@Controller
@RequestMapping("/log")
public class LogInquiryController {

    @Autowired
    private LogInquiryService logInquiryService;

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "YOU LOG SERVCE ACESS OK";
    }

    @GetMapping
    @ResponseBody
    public String getLog(@RequestBody LogRequestData data) {
        List<LogData> datas = logInquiryService.getLog(data);
        return logInquiryService.spliceLog(datas);
    }

    @GetMapping("/link")
    @ResponseBody
    public String getLink(@RequestParam(name = "logId") String logId ) {
        List<LogData> datas = logInquiryService.getLogLink(logId);
        return logInquiryService.spliceLog(datas);
    }
}
