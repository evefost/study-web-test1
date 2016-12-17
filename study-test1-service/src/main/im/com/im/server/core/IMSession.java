package com.im.server.core;

import com.im.protocol.Message;
import com.im.protocol.Message.Data;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class IMSession implements Serializable {

    public static final int TYPE_APP = 0;

    public static final int TYPE_WEB = 1;

    private static final long serialVersionUID = 1L;

    private Channel channel;
    private String clientId;
    private String clientName;
    private String clientVersion;
    private long bindTime;
    private String uid;
    private long loginTime;
    private String encriptKey;
    private int sessionType;

    private IMSession(Channel channel) {
        this.channel = channel;
    }

    public static IMSession buildSesion(ChannelHandlerContext ctx, Data data) {
        IMSession session = new IMSession(ctx.channel());
        session.setClientId(data.getClientId());
        session.setClientName(data.getClientName());
        session.setClientVersion(data.getClientVersion());
        session.setBindTime(new Date().getTime());
        session.setEncriptKey(UUID.randomUUID().toString().substring(0, 6));
        return session;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    public String getEncriptKey() {
        return encriptKey;
    }

    public void setEncriptKey(String encriptKey) {
        this.encriptKey = encriptKey;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean write(Object msg) {
        if (channel != null && channel.isActive()) {
            if (sessionType == TYPE_WEB) {
                TextWebSocketFrame txframe = null;
                if (msg instanceof Message.Data.Builder) {
                    Message.Data.Builder data = (Data.Builder) msg;


                } else if (msg instanceof Message.Data) {
                    Message.Data data = (Data) msg;
                }
            } else {
                return channel.writeAndFlush(msg).awaitUninterruptibly(5000);
            }

        }
        return false;
    }

    public void close(boolean immediately) {
        if (channel != null) {
            channel.disconnect();
            channel.close();
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public long getBindTime() {
        return bindTime;
    }

    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}