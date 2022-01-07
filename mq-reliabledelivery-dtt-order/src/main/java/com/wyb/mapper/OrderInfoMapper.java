package com.wyb.mapper;

import com.wyb.entity.OrderInfo;

public interface OrderInfoMapper {

    /**
     * 方法实现说明:订单保存
     * @author:smlz
     * @param orderInfo:订单实体
     * @return: int 插入的条数
     * @date:2019/10/11 15:04
     */
    int saveOrderInfo(OrderInfo orderInfo);
}
