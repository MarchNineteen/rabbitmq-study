package com.wyb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wyb.bo.MsgTxtBo;
import com.wyb.compent.MsgSender;
import com.wyb.entity.OrderInfo;
import com.wyb.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private MsgSender msgSender;

    @RequestMapping("/saveOrder")
    public String saveOrder() throws JsonProcessingException {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(System.currentTimeMillis());
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setUserName("smlz");
        orderInfo.setMoney(10000);
        orderInfo.setProductNo(1);

        try {
            orderInfoService.saveOrderInfoWithMessage(orderInfo);
        }
        catch (RuntimeException e) {
            return e.getMessage();
        }
        return "ok";
    }

    /**
     * 订单重试生成接口
     *
     * @return
     */
    @RequestMapping("/retryMsg")
    public String retryMsg(@RequestBody MsgTxtBo msgTxtBo) {

        log.info("消息重新发送:{}", msgTxtBo);

        //第一次发送消息
        msgSender.senderMsg(msgTxtBo);

        msgSender.senderDelayCheckMsg(msgTxtBo);

        return "ok";
    }
}
