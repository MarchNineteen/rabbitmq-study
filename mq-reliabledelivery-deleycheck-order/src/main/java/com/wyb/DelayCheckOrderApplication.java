package com.wyb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class DelayCheckOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DelayCheckOrderApplication.class, args);
	}
}
