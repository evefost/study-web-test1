package com.im.protocol.handler;


import com.big.data.service.impl.SessionManager;
import com.im.protocol.Message;
import com.im.server.core.ProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 绑定客户端信息:用户登录前
 */
public class BindClientHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Message.Data data) {
        logger.debug("绑定客户端,创建会话...");
        SessionManager.createScession(ctx, data);
    }

    @Override
    public int getCmd() {
        return Message.Data.Cmd.BIND_CLIENT_VALUE;
    }


}
