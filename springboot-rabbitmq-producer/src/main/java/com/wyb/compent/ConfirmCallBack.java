package com.wyb.compent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

@Slf4j
public class ConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationData:========>{}", correlationData.getId());
        log.info("ack:================{}", ack);
        if (ack) {
            log.info("mq生产端消息已经成功投递到了broker,更新我们消息日志表");
        } else {
            log.warn("mq生产端没有被broker ack,原因:{}", cause);
        }
    }
}
