package com.wyb.comfirm_listener;

import java.io.IOException;

public class ConfirmListener implements com.rabbitmq.client.ConfirmListener {

    /**
     * @param deliveryTag   唯一消息Id
     * @param multiple:是否批量
     * @throws IOException
     */
    @Override
    public void handleAck(long deliveryTag, boolean multiple) throws IOException {

        System.out.println("当前时间:" + System.currentTimeMillis() + "TulingConfirmListener handleAck:" + deliveryTag);
    }

    /**
     * 处理异常
     *
     * @param deliveryTag
     * @param multiple
     * @throws IOException
     */
    @Override
    public void handleNack(long deliveryTag, boolean multiple) throws IOException {
        System.out.println("TulingConfirmListener handleNack:" + deliveryTag);

    }
}
