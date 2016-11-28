package com.im.protocol.handler;

import com.im.sdk.protocol.Message;
import com.im.sdk.protocol.Message.Data;
import com.im.sdk.protocol.Message.Data.Cmd;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.XXTEA;
import io.netty.channel.ChannelHandlerContext;

public class ChatMsgHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {
        if (data.getIsEncript()) {
            String dec = XXTEA.decryptBase64StringToString(data.getContent(), data.getEncriptKey());
            System.out.println("channelRead  encript:普通消息:" + data.getContent() + "==" + dec);
        } else {
            System.out.println("channelRead  普通消息:" + data.getContent() + "==time:" + data.getCreateTime());
        }

        //先保存消息，用户收到才删除
        saveMessage(data);
        //回应客户端
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(data.getId());
        reply.setCmd(Cmd.CHAT_TXT_ECHO_VALUE);
        reply.setCreateTime(data.getCreateTime());
        ctx.writeAndFlush(reply);
        IMSession receiverSession = getSessionManager().getSession(data.getReceiverId());
        if (receiverSession != null) {
            receiverSession.write(data);
        } else {
            //该用户不存在或没在线
        }
    }

    /**
     * 保存消息
     */
    private void saveMessage(Data data) {

    }

    @Override
    public int getCmd() {
        return Cmd.CHAT_TXT_VALUE;
    }

}
