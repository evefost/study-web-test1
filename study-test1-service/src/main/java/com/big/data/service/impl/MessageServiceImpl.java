package com.big.data.service.impl;

import com.big.data.service.MessageService;
import com.im.protocol.Message;
import com.im.server.core.IMSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("messageService")
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**
     * key:clientId;
     * value:IMSession
     */
    private static HashMap<String, IMSession> sessions = new HashMap<String, IMSession>();

    /**
     * 用户maping
     * key:uid;
     * value:clientId
     */
    private static HashMap<String, String> loginUsers = new HashMap<String, String>();

    private static Map<String, SocketChannel> probufChannels = new HashMap<String, SocketChannel>();

    private static Map<String, SocketChannel> webChannels = new HashMap<String, SocketChannel>();


    public void addChannel(int type, SocketChannel channel) {
        if (type == 0) {
            probufChannels.put(channel.id().toString(), channel);
        } else if (type == 1) {
            webChannels.put(channel.id().toString(), channel);
        }

    }

    public void dispatcherMessage(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Message.Data) {
            Message.Data data = (Message.Data) msg;
            logger.debug("消息类型[probuf]:" + data.getContent());
            processProbufMessage(ctx, data);

            //sendWebFrame(data);
        } else {
            if (msg instanceof TextWebSocketFrame) {
                TextWebSocketFrame textframe = (TextWebSocketFrame) msg;
                logger.debug("消息类型[SocketFrame]:" + textframe.text());
                ctx.channel().write(new TextWebSocketFrame(textframe.text().toUpperCase()));
                //Message.Data data = JSON.parseObject(textframe.text(), Message.Data.class);


                Message.Data.Builder reply = Message.Data.newBuilder();
                Message.Data data = reply.build();
                sendProbuf(textframe.text());
            } else {
                logger.warn("消息类型[不支持该类型]");
            }
        }
    }

    public void broadCast(Message.Data data) {

    }


    private void processProbufMessage(ChannelHandlerContext ctx, Message.Data data) {
        switch (data.getCmd()) {
            case Message.Data.Cmd.LOGIN_VALUE:
                logger.debug("登录消息 :" + ctx.channel().remoteAddress());
                break;
            case Message.Data.Cmd.LOGOUT_VALUE:
                logger.debug("登出消息 :" + ctx.channel().remoteAddress());
                break;
            case Message.Data.Cmd.CHAT_TXT_VALUE:
                logger.debug("普通消息:" + data.getContent() + "==time:" + data.getCreateTime());
                break;
            case Message.Data.Cmd.HEARTBEAT_VALUE:
                logger.debug("心跳消息:");
                ctx.writeAndFlush(data);
                break;
            case Message.Data.Cmd.BIND_CLIENT_VALUE:
                logger.debug("绑定clientId:" + data.getClientId());
                break;
            default:
                break;
        }
    }

    private void sendWebFrame(ChannelHandlerContext ctx, Message.Data data) {
        Set<Map.Entry<String, SocketChannel>> entries = webChannels.entrySet();
        for (Map.Entry<String, SocketChannel> entry : entries) {
            entry.getValue().write(new TextWebSocketFrame(data.getContent()));
            entry.getValue().flush();
        }
    }

    private void sendProbuf(String text) {
        Set<Map.Entry<String, SocketChannel>> entries = probufChannels.entrySet();
        for (Map.Entry<String, SocketChannel> entry : entries) {
            sendChatTxtMsg(entry.getValue(), text);
        }
    }

    private void sendChatTxtMsg(SocketChannel channel, String content) {
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(UUID.randomUUID().toString());
        reply.setCmd(Message.Data.Cmd.CHAT_TXT_VALUE);
        reply.setCreateTime(new Date().getTime());
        reply.setContent(content);
        channel.writeAndFlush(reply);
    }
}
