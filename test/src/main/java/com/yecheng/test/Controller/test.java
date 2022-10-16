package com.yecheng.test.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class test {
    @Autowired
    private RsaService rsaService;

    @PostMapping
    @ResponseBody
    public String atest(HttpServletRequest request) throws Exception {
        System.out.println(rsaService.doDecryption(request.getInputStream()));
        return "hello";
    }
}
