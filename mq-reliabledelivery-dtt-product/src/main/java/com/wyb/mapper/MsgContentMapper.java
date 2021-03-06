package com.wyb.mapper;

import com.wyb.entity.MessageContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MsgContentMapper {

    /**
     * 方法实现说明:保存消息
     * @author:smlz
     * @param messageContent:消息对象
     * @return:
     * @date:2019/10/11 16:16
     */
    int saveMsgContent(MessageContent messageContent);

    int updateMsgStatus(MessageContent messageContent);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(String msgId);


}
