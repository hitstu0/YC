package com.yecheng.api_gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class ApiGatewayApplication {
    static Logger logger = LoggerFactory.getLogger(ApiGatewayApplication.class);
	public static void main(String[] args) {
		try {
			System.setProperty("rocketmq.client.logRoot","lo/");
			SpringApplication.run(ApiGatewayApplication.class, args);
		} catch (Exception e) {
            logger.error(e.getMessage());;
		}
	}

}
