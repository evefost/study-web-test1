package com.im.server.util;

import com.im.protocol.Message;
import com.im.server.core.IMSession;

import java.util.UUID;

public class SessionUtils {

    public static void reply(IMSession newSession, Integer cmd) {
        Message.Data.Builder reply = Message.Data.newBuilder();
        reply.setId(UUID.randomUUID().toString());
        reply.setCmd(cmd);
        reply.setCreateTime(System.currentTimeMillis());
        newSession.write(reply);
    }

}
