package com.im.protocol.handler;


import com.im.protocol.Message;
import com.im.server.core.IMSession;
import com.im.server.core.ProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

import static com.im.server.core.SessionManager.getSessionByUid;


/***
 * 登出处理
 *
 * @author mis
 */
public class LogoutHandler extends ProtocolHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, Message.Data data) {
        logger.debug("登出消息");
        try {
            IMSession ios = getSessionByUid(data.getSenderId());

//			String account =ios.getTag(CIMConstant.SESSION_KEY).toString();
//			ios.removeTag(CIMConstant.SESSION_KEY);
            //getSessionManager().removeSession(account);
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
