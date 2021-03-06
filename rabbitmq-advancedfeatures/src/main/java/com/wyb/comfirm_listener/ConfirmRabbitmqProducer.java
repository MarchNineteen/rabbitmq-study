package com.wyb.comfirm_listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class ConfirmRabbitmqProducer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.159.8");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("tuling");
        connectionFactory.setUsername("smlz");
        connectionFactory.setPassword("smlz");

        //创建一个连接
        Connection connection = connectionFactory.newConnection();

        //创建一个channel
        Channel channel = connection.createChannel();

        //设置消息投递模式(确认模式)
        channel.confirmSelect();

        //准备发送消息
        String exchangeName = "tuling.confirm.topicexchange";
        String routingKey = "tuling.confirm.key";

        //设置消息属性
        Map<String,Object> tulingInfo = new HashMap<>();
        tulingInfo.put("company","tuling");
        tulingInfo.put("location","长沙");

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .correlationId(UUID.randomUUID().toString())
                .timestamp(new Date())
                .headers(tulingInfo)
                .build();

        String msgContext = "你好 图灵....";
        /**
         * 消息确认监听
         */
        channel.addConfirmListener(new ConfirmListener());

        channel.basicPublish(exchangeName,routingKey,basicProperties,msgContext.getBytes());


        /**
         * 注意:在这里千万不能调用channel.close不然 消费就不能接受确认了
         */


    }
}
