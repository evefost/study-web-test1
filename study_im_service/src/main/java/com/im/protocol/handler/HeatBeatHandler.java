package com.im.protocol.handler;

import com.im.sdk.protocol.Message;
import com.im.sdk.protocol.Message.Data;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.XXTEA;
import io.netty.channel.ChannelHandlerContext;

public class HeatBeatHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {
        System.out.println("HeatBeatHandler=================>>ct:" + data.getContent());
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
