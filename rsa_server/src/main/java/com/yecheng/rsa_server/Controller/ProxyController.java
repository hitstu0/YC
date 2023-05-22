package com.yecheng.rsa_server.Controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yecheng.rsa_server.Service.RsaService;

@Controller
public class ProxyController {

    @Value("${target_ip}")
    private String targetIp;

    @Value("${target_port}")
    private String port;

    @Autowired
    private RsaService rsaService;

    @RequestMapping(value = "/proxy/**")
    @ResponseBody
    public String doProxy(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        String targetAddr = targetIp + ":" + port;
        //创建请求URL
        URI uri = new URI(request.getRequestURI());
        String path = uri.getPath();
        String query = request.getQueryString();
        String target = targetAddr + path.replace("/proxy", "");
        if (query != null && !query.equals("") && !query.equals("null")) {
            target = target + "?" + query;
        }
        URL url = new URL(target);
        
        //获取连接对象
        HttpURLConnection con = null;
        try {
        con = (HttpURLConnection) url.openConnection();

        //设置请求方法
        con.setRequestMethod(request.getMethod());
        con.setDoInput(true);
        con.setDoOutput(true);
       
        
        // 设置请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String value = request.getHeader(headerName); 
            con.setRequestProperty(headerName, value);
        }

        //加密数据
        rsaService.doEncryption(request.getInputStream(), con.getOutputStream());
        
        
        //执行远程调用
        int responseCode = con.getResponseCode();
        Set<String> headerMap = con.getHeaderFields().keySet();

        //设置响应
        response.setStatus(responseCode);

        Iterator<String> iterator = headerMap.iterator();
        while(iterator.hasNext()) {
            String name = iterator.next();
            response.setHeader(name, con.getHeaderField(name));
        }
        
        //解密
        return rsaService.doDecryption(con.getInputStream());
    } finally {
        con.disconnect();
    }
    }
}
