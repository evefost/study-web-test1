package com.im.server.core;

import com.im.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyang
 */
public abstract class ProtocolHandler {

    protected static final Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);

    public abstract void handleRequest(ChannelHandlerContext ctx, Message.Data data);

    public abstract int getCmd();

}
