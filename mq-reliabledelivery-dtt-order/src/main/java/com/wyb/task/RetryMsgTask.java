package com.wyb.task;

import com.wyb.bo.MsgTxtBo;
import com.wyb.compent.MsgSender;
import com.wyb.constants.MqConst;
import com.wyb.entity.MessageContent;
import com.wyb.enumuration.MsgStatusEnum;
import com.wyb.mapper.MsgContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RetryMsgTask {

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private MsgContentMapper msgContentMapper;

    /**
     * 延时5s启动
     * 周期10S一次
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void retrySend() {
        System.out.println("-----------------------------");
        //查询五分钟消息状态还没有完结的消息
        List<MessageContent> messageContentList = msgContentMapper.qryNeedRetryMsg(MsgStatusEnum.CONSUMER_SUCCESS.getCode(), MqConst.TIME_DIFF);

        for (MessageContent messageContent : messageContentList) {

            if (messageContent.getMaxRetry() > messageContent.getCurrentRetry()) {
                MsgTxtBo msgTxtBo = new MsgTxtBo();
                msgTxtBo.setMsgId(messageContent.getMsgId());
                msgTxtBo.setProductNo(messageContent.getProductNo());
                msgTxtBo.setOrderNo(messageContent.getOrderNo());
                //更新消息重试次数
                msgContentMapper.updateMsgRetryCount(msgTxtBo.getMsgId());
                msgSender.senderMsg(msgTxtBo);
            } else {
                log.warn("消息:{}以及达到最大重试次数", messageContent);
            }

        }
    }
}
