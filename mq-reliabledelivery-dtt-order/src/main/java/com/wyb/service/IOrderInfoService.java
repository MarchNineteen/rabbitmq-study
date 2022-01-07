package com.wyb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wyb.entity.MessageContent;
import com.wyb.entity.OrderInfo;

public interface IOrderInfoService {

    /**
     * 方法实现说明:订单保存
     * @author:smlz
     * @param orderInfo:订单实体
     * @return: int 插入的条数
     * @date:2019/10/11 15:04
     */
    void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent);

    void saveOrderInfoWithMessage(OrderInfo orderInfo) throws JsonProcessingException;
}
