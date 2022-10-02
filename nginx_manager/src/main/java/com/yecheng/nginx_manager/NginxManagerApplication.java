package com.yecheng.nginx_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class NginxManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NginxManagerApplication.class, args);
	}

}
