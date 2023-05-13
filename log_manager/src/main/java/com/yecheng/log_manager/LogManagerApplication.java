package com.yecheng.log_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class LogManagerApplication {

	public static void main(String[] args) {
		System.setProperty("rocketmq.client.logRoot","lo/");

		SpringApplication.run(LogManagerApplication.class, args);
	}

}
