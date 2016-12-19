package com.big.data.service.impl;

import com.googlecode.protobuf.format.JsonFormat;
import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.ProtocolHandllerLoader;
import com.im.server.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);


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


    /**
     * bind的通导
     * key:channeladdress;
     * value:clientId
     */
    private static HashMap<String, String> bindChannels = new HashMap<String, String>();


    public static void dispatcherMessage(ChannelHandlerContext ctx, Object msg) {
        Message.Data data = null;
        if (msg instanceof Message.Data) {
            data = (Message.Data) msg;
            logger.debug("消息类型[probuf]:" + data.getContent());
        } else if (msg instanceof TextWebSocketFrame) {
            logger.debug("消息类型[TextWebSocketFrame]");
            TextWebSocketFrame textframe = (TextWebSocketFrame) msg;
            String jsonFormat = textframe.text();
            Message.Data.Builder builder = Message.Data.newBuilder();
            try {
                logger.debug("json转成probuf[" + builder.getContent());
                JsonFormat.merge(jsonFormat, builder);
                data = builder.build();
            } catch (JsonFormat.ParseException e) {
                logger.error("TextWebSocketFrame 消息解码失败");
            }
        } else if (msg instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) msg;
            ByteBuf buf = binaryWebSocketFrame.content();
            byte[] b = new byte[buf.readableBytes()];
            buf.readBytes(b);
            try {
                data = Message.Data.parseFrom(b);
                logger.debug("消息类型[BinaryWebSocketFrame]" + data.getContent());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("BinaryWebSocketFrame 消息解码失败");
            }
        } else {
            logger.warn("消息类型[不支持该类型]");
        }
        ProtocolHandler protocolHandler = ProtocolHandllerLoader.getProtocolHandler(data.getCmd());
        if (protocolHandler != null && data != null) {
            protocolHandler.handleRequest(ctx, data);
        }
    }

    public static void createScession(ChannelHandlerContext ctx, Message.Data data) {
        IMSession newSession = IMSession.buildSesion(ctx, data);
        sessions.put(newSession.getClientId(), newSession);
        bindChannels.put(ctx.channel().remoteAddress().toString(),data.getClientId());
        reply(newSession,data.getId(), data.getCmd());
    }


    public static void onLogin(IMSession newSession) {
        sessions.put(newSession.getClientId(), newSession);
        loginUsers.put(newSession.getUid(), newSession.getClientId());
    }

    public static IMSession getSession(String clientId) {
        return sessions.get(clientId);
    }

    public static IMSession getSessionByUid(String uid) {
        String clientId = loginUsers.get(uid);
        return sessions.get(clientId);
    }

    public static void removeSession(IMSession session) {
        sessions.remove(session.getClientId());
        loginUsers.remove(session.getUid());
        session.close(true);
    }

    public static boolean isAreadyLogin(Message.Data data) {
        String clientId = data.getClientId();
        String uid = data.getSenderId();
        String oldClientId = loginUsers.get(uid);
        //同一个uid,不同的clientId,
        if (!StringUtils.isEmpty(oldClientId) && !oldClientId.equals(clientId)) {
            return true;
        }
        return false;
    }


    public static void reply(IMSession newSession,String msgId, Integer cmd) {
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(msgId);
        reply.setCmd(cmd);
        reply.setCreateTime(System.currentTimeMillis());
        newSession.write(reply);
    }

    public static void onChannelClose(ChannelHandlerContext ctx){
        String clienId = bindChannels.get(ctx.channel().remoteAddress().toString());
        if(clienId != null){
            IMSession imSession = sessions.get(clienId);
            sessions.remove(clienId);
            String uid = imSession.getUid();
            if(uid != null){
                loginUsers.remove(uid);
            }
        }
    }

}
