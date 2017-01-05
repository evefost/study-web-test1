package com.im.protocol.handler;

import com.im.protocol.Message;
import com.im.protocol.Message.Data;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.UUID;

import static com.im.server.core.SessionManager.*;


public class LoginHandler extends ProtocolHandler {


    @Override
    public int getCmd() {
        return Message.Data.Cmd.LOGIN_VALUE;
    }

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {
        logger.debug("登录消息");
        String clientId = data.getClientId();
        IMSession oldSession = getSessionByUid(data.getSenderId());
        if (oldSession != null) {
            if (clientId.equals(oldSession.getClientId())) {
                logger.debug("已经登录过,同一台机器:{}", data.getClientId());
                Message.Data.Builder reply = Message.Data.newBuilder();
                reply.setId(UUID.randomUUID().toString());
                reply.setCmd(Message.Data.Cmd.LOGIN_ECHO_VALUE);
                reply.setCreateTime(data.getCreateTime());
                reply.setContent("已经登录过");
                oldSession.write(reply.build());
                return;
            } else {
                logger.debug("另一台机器登录:{}", data.getClientId());
                Message.Data.Builder offLineReply = Message.Data.newBuilder();
                offLineReply.setId(UUID.randomUUID().toString());
                offLineReply.setCmd(Message.Data.Cmd.OTHER_LOGGIN_VALUE);
                offLineReply.setCreateTime(data.getCreateTime());
                offLineReply.setContent("帐号在另一台机器登录");
                oldSession.write(offLineReply.build());
                removeSession(oldSession);
            }

        }

        // 创建新的用户信息
        IMSession newSession = getSession(data.getClientId());
        if (newSession == null) {
            newSession = createScession(ctx, data);
        }
        newSession.setUid(data.getSenderId());
        newSession.setLoginTime(System.currentTimeMillis());
        onLogin(newSession);
        logger.debug("登录成功,回应客户端:{}", data.getClientId());
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(UUID.randomUUID().toString());
        reply.setCmd(Message.Data.Cmd.LOGIN_ECHO_VALUE);
        reply.setCreateTime(System.currentTimeMillis());
        reply.setContent("登录成功");
        newSession.write(reply.build());

    }


    /**
     * 检查并发送离线消息
     */
    private void checkAndSendOffLineMessages(IMSession newSession) {

    }


}
