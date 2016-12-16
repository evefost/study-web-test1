package com.big.data.service.impl;

import com.im.protocol.JsonMsg;
import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.util.SessionUtils;
import com.im.server.util.StringUtils;
import com.im.server.util.XXTEA;
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

        if (msg instanceof Message.Data) {
            Message.Data data = (Message.Data) msg;
            logger.debug("消息类型[probuf]:" + data.getContent());
            processMessage(ctx, data);
        } else if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textframe = (TextWebSocketFrame) msg;
            logger.debug("消息类型[SocketFrame]:" + textframe.text());

            ctx.channel().write(new TextWebSocketFrame(textframe.text().toUpperCase()));
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
            Message.Data data = reply.build();
            processMessage(ctx, data);
        } else {
            logger.warn("消息类型[不支持该类型]");
        }
    }

    private static void processMessage(ChannelHandlerContext ctx, Message.Data data) {
        switch (data.getCmd()) {
            case Message.Data.Cmd.LOGIN_VALUE:
                logger.debug("登录消息 :" + ctx.channel().remoteAddress());
                loginHandle(ctx, data);
                break;
            case Message.Data.Cmd.LOGOUT_VALUE:
                logger.debug("登出消息 :" + ctx.channel().remoteAddress());
                break;
            case Message.Data.Cmd.CHAT_TXT_VALUE:
                logger.debug("普通消息:" + data.getContent() + "==time:" + data.getCreateTime());
                chatMessageHandle(ctx,data);
                break;
            case Message.Data.Cmd.HEARTBEAT_VALUE:
                logger.debug("心跳消息:");
                ctx.writeAndFlush(data);
                break;
            case Message.Data.Cmd.BIND_CLIENT_VALUE:
                logger.debug("绑定clientId:" + data.getClientId());
                createSession(ctx, data);
                break;
            default:
                break;
        }
    }

    public static void createSession(ChannelHandlerContext ctx, Message.Data data) {
        logger.debug("创建会话...");
        IMSession newSession = IMSession.buildSesion(ctx, data);
        SessionUtils.reply(newSession, data.getCmd());
        sessions.put(newSession.getClientId(), newSession);
        bindChannels.put(ctx.channel().remoteAddress().toString(),data.getClientId());
        reply(newSession,data.getId(), data.getCmd());
    }


    private static void loginHandle(ChannelHandlerContext ctx, Message.Data data) {
        logger.debug("处理用户登录...");
        if (isAreadyLogin(data)) {
            // 让另一端下线,并清除以前的session
            Message.Data.Builder offLineReply = Message.Data.newBuilder();
            offLineReply.setId(UUID.randomUUID().toString());
            offLineReply.setCmd(Message.Data.Cmd.OTHER_LOGGIN_VALUE);
            offLineReply.setCreateTime(data.getCreateTime());
            offLineReply.setReceiverId(data.getSenderId());
            IMSession oldSession = sessions.get(data.getClientId());
            oldSession.write(offLineReply);
            removeSession(oldSession);
        }
        // 创建新的用户信息
        IMSession newSession = sessions.get(data.getClientId());
        if (newSession == null) {
            newSession = IMSession.buildSesion(ctx, data);
        }
        newSession.setUid(data.getSenderId());
        newSession.setLoginTime(System.currentTimeMillis());
        sessions.put(newSession.getClientId(), newSession);
        loginUsers.put(data.getSenderId(), data.getClientId());
        logger.debug("登录成功,回应客户端:" + data.getSenderId());
        reply(newSession,UUID.randomUUID().toString(),Message.Data.Cmd.LOGIN_ECHO_VALUE);

    }
    public static void chatMessageHandle(ChannelHandlerContext ctx, Message.Data data) {
        //回应客户端
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(data.getId());
        reply.setCmd(Message.Data.Cmd.CHAT_TXT_ECHO_VALUE);
        reply.setCreateTime(data.getCreateTime());
        ctx.writeAndFlush(reply);
        String receiverId = loginUsers.get(data.getReceiverId());
        IMSession receiverSession = sessions.get(receiverId);
        if (receiverSession != null) {
            receiverSession.write(data);
        } else {
            //该用户不存在或没在线
        }
    }

    private static void removeSession(IMSession session) {
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
