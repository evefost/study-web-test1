package com.im.server.core;

import com.im.manage.session.ContextHolder;
import com.im.manage.session.SessionManager;
import com.im.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xieyang
 */
public abstract class ProtocolHandler {

    public abstract void handleRequest(ChannelHandlerContext ctx, Message.Data data);

    public abstract int getCmd();

    public SessionManager getSessionManager() {
        return ((SessionManager) ContextHolder.getBean("defaultSessionManager"));
    }
}
