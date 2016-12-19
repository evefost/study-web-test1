package com.im.protocol.handler;

import com.big.data.service.impl.SessionManager;
import com.im.protocol.Message;
import com.im.protocol.Message.Data;
import com.im.protocol.Message.Data.Cmd;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

import static com.big.data.service.impl.SessionManager.getSessionByUid;

public class ChatMsgHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {
        logger.debug("普通聊天消息");

        //先保存消息，用户收到才删除
        saveMessage(data);
        //回应客户端
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(data.getId());
        reply.setCmd(Cmd.CHAT_TXT_ECHO_VALUE);
        reply.setCreateTime(data.getCreateTime());
        ctx.writeAndFlush(reply);

        IMSession receiverSession = getSessionByUid(data.getReceiverId());
        if (receiverSession != null) {
            receiverSession.write(data);
        } else {
            //该用户不存在或没在线
            logger.debug("该用户不存在或没在线...");
        }
        SessionManager.bradcast(data);
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
