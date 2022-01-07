package com.wyb.service.impl;

import com.wyb.bo.MsgTxtBo;
import com.wyb.entity.MessageContent;
import com.wyb.enumration.MsgStatusEnum;
import com.wyb.exception.BizExp;
import com.wyb.mapper.MsgContentMapper;
import com.wyb.mapper.ProductInfoMapper;
import com.wyb.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Transactional
    @Override
    public boolean updateProductStore(MsgTxtBo msgTxtBo) {
        boolean updateFlag = true;
        try{
            //更新库存
            productInfoMapper.updateProductStoreById(msgTxtBo.getProductNo());

            //更新消息表状态
            MessageContent messageContent = new MessageContent();
            messageContent.setMsgId(msgTxtBo.getMsgId());
            messageContent.setUpdateTime(new Date());
            messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_SUCCESS.getCode());
            msgContentMapper.updateMsgStatus(messageContent);

            //System.out.println(1/0);
        }catch (Exception e) {
            log.error("更新数据库失败:{}",e);
            throw new BizExp(0,"更新数据库异常");
        }
        return updateFlag;
    }
}
