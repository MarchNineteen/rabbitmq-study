package com.wyb.rabbitmqwithspring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainStart {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqConfig.class);
    }
}
