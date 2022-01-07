package com.wyb.service.impl;

import com.wyb.bo.MsgTxtBo;
import com.wyb.exception.BizExp;
import com.wyb.mapper.ProductInfoMapper;
import com.wyb.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Transactional
    @Override
    public boolean updateProductStore(MsgTxtBo msgTxtBo) {
        boolean updateFlag = true;
        try {
            //更新库存
            productInfoMapper.updateProductStoreById(msgTxtBo.getProductNo());
            //System.out.println(1/0);
        } catch (Exception e) {
            log.error("更新数据库失败:{}", e);
            throw new BizExp(0, "更新数据库异常");
        }
        return updateFlag;
    }
}
