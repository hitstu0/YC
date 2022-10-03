package com.yecheng.api_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class ApiManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiManagerApplication.class, args);
	}

}
