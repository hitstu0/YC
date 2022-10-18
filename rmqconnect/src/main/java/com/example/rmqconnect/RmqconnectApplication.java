package com.example.rmqconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RmqconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmqconnectApplication.class, args);
	}

}
