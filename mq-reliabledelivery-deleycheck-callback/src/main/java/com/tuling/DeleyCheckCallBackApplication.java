package com.tuling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DeleyCheckCallBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeleyCheckCallBackApplication.class, args);
	}

}
