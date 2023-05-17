package com.yecheng.log_manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;



@SpringBootApplication(
	exclude = {DataSourceAutoConfiguration.class}, 
    scanBasePackages = {"com.yecheng"}
)
public class LogManagerApplication {
    static Logger logger = LoggerFactory.getLogger(LogManagerApplication.class);
	public static void main(String[] args) {
		System.setProperty("rocketmq.client.logRoot","lo/");
        try{
		SpringApplication.run(LogManagerApplication.class, args);
		} catch(Exception e) {
		   e.printStackTrace();
           logger.error(e.getMessage());
		}
	}

}
