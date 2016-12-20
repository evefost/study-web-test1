package com.im.protocol.handler;


import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

import static com.big.data.service.impl.SessionManager.createScession;
import static com.big.data.service.impl.SessionManager.getSession;

/**
 * 绑定客户端信息:用户登录前
 */
public class BindClientHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Message.Data data) {
        logger.debug("绑定客户端,消息...");
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(data.getId());
        reply.setCmd(Message.Data.Cmd.BIND_CLIENT_VALUE);
        reply.setCreateTime(System.currentTimeMillis());
        IMSession session = getSession(data.getClientId());
        if (session != null) {
            logger.debug("已经绑定过:{}", data.getClientId());
            reply.setContent("已经绑定过");
            session.write(reply.build());
            return;
        }
        logger.debug("绑定{}客户端,创建会话...", data.getClientName());
        IMSession newSession = createScession(ctx, data);
        reply.setContent("绑定成功");
        newSession.write(reply.build());
    }

    @Override
    public int getCmd() {
        return Message.Data.Cmd.BIND_CLIENT_VALUE;
    }


}
