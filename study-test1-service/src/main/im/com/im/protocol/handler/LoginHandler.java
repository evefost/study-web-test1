package com.im.protocol.handler;

import com.im.sdk.protocol.Message;
import com.im.sdk.protocol.Message.Data;
import com.im.sdk.protocol.Message.Data.Cmd;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.UUID;

public class LoginHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {

        System.out.println("LoginHandler=================>>");
        if (getSessionManager().isAreadyLogin(data)) {
            // 让另一端下线,并清除以前的session
            Message.Data.Builder offLineReply = Message.Data.newBuilder();
            offLineReply.setId(UUID.randomUUID().toString());
            offLineReply.setCmd(Cmd.OTHER_LOGGIN_VALUE);
            offLineReply.setCreateTime(data.getCreateTime());
            offLineReply.setReceiverId(data.getSenderId());
            IMSession oldSession = getSessionManager().getSession(data.getSenderId());
            oldSession.write(offLineReply);
            getSessionManager().removeSession(oldSession);
        }
        // 创建新的用户信息
        IMSession newSession = getSessionManager().getSession(data.getClientId());
        if (newSession == null) {
            newSession = IMSession.buildSesion(ctx, data);
        }
        newSession.setUid(data.getSenderId());
        newSession.setLoginTime(System.currentTimeMillis());
        System.out.println("LoginHandler 登录成功,回应客户端:" + data.getSenderId());
        getSessionManager().addSession(newSession);
        SessionUtils.reply(newSession, Message.Data.Cmd.LOGIN_ECHO_VALUE);

        checkAndSendOffLineMessages(newSession);

    }

    /**
     * 检查并发送离线消息
     */
    private void checkAndSendOffLineMessages(IMSession newSession) {

    }

    @Override
    public int getCmd() {
        return Message.Data.Cmd.LOGIN_VALUE;
    }

}
