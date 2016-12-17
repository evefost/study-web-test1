package com.im.protocol.handler;


import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import com.im.server.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;

import static com.big.data.service.impl.SessionManager.getSessionByUid;

/***
 * 登出处理
 *
 * @author mis
 */
public class LogoutHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Message.Data data) {

        try {
            IMSession ios = getSessionByUid(data.getSenderId());

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
        return Message.Data.Cmd.LOGOUT_VALUE;
    }

}
