package com.im.manage.push;

import com.im.sdk.protocol.Message;

/**
 * 推送消息接口
 *
 * @author xie
 */
public interface IMessagePusher {

    /**
     * 推送消息
     *
     * @param msg
     */
    void pushMessage(Message.Data.Builder msg);

}
