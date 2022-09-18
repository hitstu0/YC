package com.yecheng.ykv.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.ykv.Data.CodeMsg;
import com.yecheng.ykv.Data.Kv;
import com.yecheng.ykv.Service.KvService;
import com.yecheng.ykv.Service.UserService;

@Controller
@RequestMapping("/ykv")
public class KvController {
    Logger logger = LoggerFactory.getLogger(KvController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private KvService kvService;

    @PostMapping
    @ResponseBody
    public CodeMsg setKv(HttpServletRequest req, HttpServletResponse response, @RequestBody Kv kvData) {
        logger.info("begin set kv, key:{}, value:{}", kvData.getDbkey(), kvData.getValue());
        if (!userService.judgeUser(req)) {
            return CodeMsg.UserNotLogin;
        }
         
        return kvService.setKv(kvData, response);
    }

    @GetMapping
    @ResponseBody
    public CodeMsg getValue(HttpServletRequest req, 
    @RequestParam(name = "key") String key, @RequestParam(name = "hash", required = false) Integer hash) {
        logger.info("begin getvalue, key:{}", key);
        if (!userService.judgeUser(req)) {
            return CodeMsg.UserNotLogin;
        }

        return kvService.getValue(key, hash);
    }

   
}
