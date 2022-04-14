package com.wyb.service.impl;

import com.wyb.bo.MsgTxtBo;
import com.wyb.compent.MsgSender;
import com.wyb.constants.MqConst;
import com.wyb.entity.MessageContent;
import com.wyb.entity.OrderInfo;
import com.wyb.enumuration.MsgStatusEnum;
import com.wyb.mapper.OrderInfoMapper;
import com.wyb.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @vlog: 高于生活，源于生活
 * @desc: 类的描述
 * @author: smlz
 * @createDate: 2019/10/11 15:29
 * @version: 1.0
 */
@Slf4j
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MsgSender msgSender;

    @Resource
    private IOrderInfoService orderInfoService;


    /**
     * 事务回滚 不发送消息
     * @param orderInfo:订单实体
     */
    @Transactional
    @Override
    public void saveOrderInfo(OrderInfo orderInfo) {

        try {
//            int a= 1/0;
            orderInfoMapper.saveOrderInfo(orderInfo);
        } catch (Exception e) {
            log.error("操作数据库失败:{}", e);
            throw new RuntimeException("操作数据库失败");
        }
    }

    @Override
    public void saveOrderInfoWithMessage(OrderInfo orderInfo) {

        //构建消息对象
        MessageContent messageContent = builderMessageContent(orderInfo.getOrderNo(), orderInfo.getProductNo());

        //保存数据库
        orderInfoService.saveOrderInfo(orderInfo);

        //构建消息发送对象
        MsgTxtBo msgTxtBo = new MsgTxtBo();
        msgTxtBo.setMsgId(messageContent.getMsgId());
        msgTxtBo.setOrderNo(orderInfo.getOrderNo());
        msgTxtBo.setProductNo(orderInfo.getProductNo());

        //第一次发送消息
        msgSender.senderMsg(msgTxtBo);

        //TODO:发送延时消息
        msgSender.senderDelayCheckMsg(msgTxtBo);
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
