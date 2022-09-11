package com.yecheng.ykv.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yecheng.ykv.Redis.JedisBuilder;

import redis.clients.jedis.Jedis;

@Service
public class UserService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JedisBuilder jedisBuilder;

    public boolean judgeUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            logger.error("request not contain user cookie");
            return false;
        }

        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }
        if (token == null) {
            logger.error("user token is null");
            return false;
        }
        
        Jedis jedis = null;
        String userJson = null;
        try {
           jedis = jedisBuilder.getJedis();
           userJson = jedis.get(token);
        } catch (Exception e) {
            logger.error("get redis token err:{}", e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        if (userJson == null || userJson.length() == 0) {
            return false;
        }

        logger.info("user login success, user is :{}", userJson);
        return true;
    }
}
