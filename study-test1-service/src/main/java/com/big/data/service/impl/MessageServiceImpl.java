package com.big.data.service.impl;

import com.big.data.service.MessageService;
import com.im.sdk.protocol.Message;
import com.im.server.web.WebSocketServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("messageService")
public class MessageServiceImpl  implements MessageService{
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    public void dispatcherMessage(ChannelHandlerContext ctx, Object msg) {
            if(msg instanceof Message.Data){
                Message.Data data = (Message.Data) msg;
                logger.debug("probuf 类型消息:"+data.getContent());
            }else {
                if (msg instanceof TextWebSocketFrame) {
                    TextWebSocketFrame textframe = (TextWebSocketFrame) msg;
                    logger.debug("websocket 类型消息:"+msg.toString());
                } else {
                    logger.warn("不支持的消息类型");
                }
            }
    }
}
