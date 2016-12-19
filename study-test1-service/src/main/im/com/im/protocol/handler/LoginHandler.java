package com.im.protocol.handler;

import com.im.protocol.Message;
import com.im.protocol.Message.Data;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.UUID;

import static com.big.data.service.impl.SessionManager.*;

public class LoginHandler extends ProtocolHandler {


    @Override
    public int getCmd() {
        return Message.Data.Cmd.LOGIN_VALUE;
    }

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {
        logger.debug("登录消息");
        if (isAreadyLogin(data)) {
            // 让另一端下线,并清除以前的session
            Message.Data.Builder offLineReply = Message.Data.newBuilder();
            offLineReply.setId(UUID.randomUUID().toString());
            offLineReply.setCmd(Message.Data.Cmd.OTHER_LOGGIN_VALUE);
            offLineReply.setCreateTime(data.getCreateTime());
            offLineReply.setReceiverId(data.getSenderId());
            IMSession oldSession = getSession(data.getClientId());
            oldSession.write(offLineReply.build());
            removeSession(oldSession);
        }

        // 创建新的用户信息
        IMSession newSession = getSession(data.getClientId());
        if (newSession == null) {
            newSession = IMSession.buildSesion(ctx, data);
        }
        newSession.setUid(data.getSenderId());
        newSession.setLoginTime(System.currentTimeMillis());
        onLogin(newSession);
        logger.debug("登录成功,回应客户端:" + data.getSenderId());
        reply(newSession, UUID.randomUUID().toString(), Message.Data.Cmd.LOGIN_ECHO_VALUE);

    }

    /**
     * 检查并发送离线消息
     */
    private void checkAndSendOffLineMessages(IMSession newSession) {

    }


}
