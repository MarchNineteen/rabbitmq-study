package com.wyb.compent;

import com.wyb.entity.MessageContent;
import com.wyb.enumuration.MsgStatusEnum;
import com.wyb.mapper.MsgContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MsgConfirm implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId = correlationData.getId();

        if (ack) {
            log.info("消息Id:{}对应的消息被broker签收成功", msgId);
            updateMsgStatusWithAck(msgId);
        } else {
            log.warn("消息Id:{}对应的消息被broker签收失败:{}", msgId, cause);
            updateMsgStatusWithNack(msgId, cause);
        }
    }

    /**
     * 方法实现说明:更新消息表状态为
     *
     * @param msgId:消息ID
     * @author:smlz
     * @exception:
     * @date:2019/10/11 18:01
     */
    private void updateMsgStatusWithAck(String msgId) {
        MessageContent messageContent = builderUpdateContent(msgId);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING_SUCCESS.getCode());
        msgContentMapper.updateMsgStatus(messageContent);
    }

    private void updateMsgStatusWithNack(String msgId, String cause) {

        MessageContent messageContent = builderUpdateContent(msgId);

        messageContent.setMsgStatus(MsgStatusEnum.SENDING_FAIL.getCode());
        messageContent.setErrCause(cause);
        msgContentMapper.updateMsgStatus(messageContent);
    }

    private MessageContent builderUpdateContent(String msgId) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMsgId(msgId);
        messageContent.setUpdateTime(new Date());
        return messageContent;
    }

}
