package com.big.data.service.impl;

import com.im.protocol.JsonMsg;
import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.ProtocolHandllerLoader;
import com.im.server.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

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
            TextWebSocketFrame textframe = (TextWebSocketFrame) msg;
            logger.debug("消息类型[SocketFrame]:" + textframe.text());
            ctx.channel().write(new TextWebSocketFrame(textframe.text().toUpperCase()));

            //消息转换:probuf统一处理
            JsonMsg jsonMsg = new JsonMsg();
            jsonMsg.setId(UUID.randomUUID().toString());
            jsonMsg.setCmd(Message.Data.Cmd.BIND_CLIENT_VALUE);
            jsonMsg.setContent("ab消息类型");
            jsonMsg.setCreateTime(new Date().getTime());
            jsonMsg.setClientId(UUID.randomUUID().toString());
            Message.Data.Builder reply = Message.Data.newBuilder();
            reply.setId(jsonMsg.getId());
            reply.setCmd(jsonMsg.getCmd());
            reply.setContent(jsonMsg.getContent());
            reply.setCreateTime(jsonMsg.getCreateTime());
            reply.setClientId(jsonMsg.getClientId());
            data = reply.build();
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
