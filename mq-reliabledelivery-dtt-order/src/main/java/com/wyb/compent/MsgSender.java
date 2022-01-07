package com.wyb.compent;

import com.wyb.bo.MsgTxtBo;
import com.wyb.constants.MqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MsgSender implements InitializingBean {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MsgConfirm msgConfirm;

    @Autowired
    private MsgReturnListener msgReturnListener;


    /**
     * 方法实现说明:真正的发送消息
     * @author:smlz
     * @param msgTxtBo:发送的消息对象
     * @return:
     * @exception:
     * @date:2019/10/11 20:01
     */
    public  void senderMsg(MsgTxtBo msgTxtBo){

        log.info("发送的消息ID:{}",msgTxtBo.getMsgId());

        CorrelationData correlationData = new CorrelationData(msgTxtBo.getMsgId());

        rabbitTemplate.convertAndSend(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME,MqConst.ORDER_TO_PRODUCT_ROUTING_KEY,msgTxtBo,correlationData);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(msgConfirm);
        rabbitTemplate.setReturnCallback(msgReturnListener);
        //设置消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    }
}
