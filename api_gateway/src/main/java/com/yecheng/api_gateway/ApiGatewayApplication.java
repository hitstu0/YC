package com.yecheng.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		System.setProperty("rocketmq.client.logRoot","lo/");
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
