package com.wyb.mapper;

import com.wyb.entity.MessageContent;

public interface MsgContentMapper {

    /**
     * 方法实现说明:保存消息
     *
     * @param messageContent:消息对象
     */
    int saveMsgContent(MessageContent messageContent);

    MessageContent qryMessageContentById(String msgId);

}
