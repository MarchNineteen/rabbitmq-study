package com.wyb.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wyb.bo.MsgTxtBo;
import com.wyb.compent.MsgSender;
import com.wyb.constants.MqConst;
import com.wyb.entity.MessageContent;
import com.wyb.entity.OrderInfo;
import com.wyb.enumuration.MsgStatusEnum;
import com.wyb.mapper.MsgContentMapper;
import com.wyb.mapper.OrderInfoMapper;
import com.wyb.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Autowired
    private MsgSender msgSender;

    @Transactional
    @Override
    public void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent) {

        try {
            orderInfoMapper.saveOrderInfo(orderInfo);

            //插入消息表
            msgContentMapper.saveMsgContent(messageContent);

        } catch (Exception e) {
            log.error("操作数据库失败:{}", e);
            throw new RuntimeException("操作数据库失败");
        }
    }

    @Override
    public void saveOrderInfoWithMessage(OrderInfo orderInfo) throws JsonProcessingException {

        //构建消息对象
        MessageContent messageContent = builderMessageContent(orderInfo.getOrderNo(), orderInfo.getProductNo());

        //保存数据库
        saveOrderInfo(orderInfo, messageContent);

        //构建消息发送对象
        MsgTxtBo msgTxtBo = new MsgTxtBo();
        msgTxtBo.setMsgId(messageContent.getMsgId());
        msgTxtBo.setOrderNo(orderInfo.getOrderNo());
        msgTxtBo.setProductNo(orderInfo.getProductNo());

        //发送消息
        msgSender.senderMsg(msgTxtBo);
    }


    /**
     * 方法实现说明:构建消息对象
     *
     * @author:smlz
     * @return:MessageContent 消息实体
     * @exception:
     * @date:2019/10/11 17:24
     */
    private MessageContent builderMessageContent(long orderNo, Integer productNo) {
        MessageContent messageContent = new MessageContent();
        String msgId = UUID.randomUUID().toString();
        messageContent.setMsgId(msgId);
        messageContent.setCreateTime(new Date());
        messageContent.setUpdateTime(new Date());
        messageContent.setExchange(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME);
        messageContent.setRoutingKey(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING.getCode());
        messageContent.setOrderNo(orderNo);
        messageContent.setProductNo(productNo);
        messageContent.setMaxRetry(MqConst.MSG_RETRY_COUNT);
        return messageContent;
    }
}
