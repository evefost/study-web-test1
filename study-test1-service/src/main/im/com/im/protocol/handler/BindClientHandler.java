package com.im.protocol.handler;


import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;

public class BindClientHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Message.Data data) {
        IMSession newSession = IMSession.buildSesion(ctx, data);
        getSessionManager().addSession(IMSession.buildSesion(ctx, data));
        SessionUtils.reply(newSession, data.getCmd());
    }

    @Override
    public int getCmd() {
        return Message.Data.Cmd.BIND_CLIENT_VALUE;
    }

}
