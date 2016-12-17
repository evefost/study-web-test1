package com.im.protocol.handler;

import com.im.protocol.Message;
import com.im.protocol.Message.Data;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.XXTEA;
import io.netty.channel.ChannelHandlerContext;

public class HeatBeatHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {
        logger.debug("收到客户端心跳");
        try {
            String key = "123";
            String dstring = new String(XXTEA.decrypt(data.getBody().toByteArray(), key));
            System.out.println("decrpt:" + dstring);
        } catch (Exception e) {
            System.out.println("Ex:" + e.toString());
        }
        ctx.writeAndFlush(data);
    }

    @Override
    public int getCmd() {
        return Message.Data.Cmd.HEARTBEAT_VALUE;
    }

}
