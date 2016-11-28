package com.im.protocol.handler;

import com.im.sdk.protocol.Message;
import com.im.sdk.protocol.Message.Data;
import com.im.sdk.protocol.Message.Data.Cmd;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;

/***
 * 登出处理
 *
 * @author mis
 */
public class LogoutHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Data data) {

        try {
            IMSession ios = getSessionManager().getSession(data.getSenderId());

//			String account =ios.getTag(CIMConstant.SESSION_KEY).toString();
//			ios.removeTag(CIMConstant.SESSION_KEY);
            //getSessionManager().removeSession(account);
            SessionUtils.reply(ios, Message.Data.Cmd.LOGOUT_VALUE);
            ios.close(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCmd() {
        return Cmd.LOGOUT_VALUE;
    }

}
