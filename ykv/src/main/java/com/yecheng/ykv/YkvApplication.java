package com.yecheng.ykv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class YkvApplication {

	public static void main(String[] args) {
		SpringApplication.run(YkvApplication.class, args);
	}

}
