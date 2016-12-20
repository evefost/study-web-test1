package com.im.server.core;

import com.googlecode.protobuf.format.JsonFormat;
import com.im.protocol.Message;
import com.im.protocol.Message.Data;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import static com.googlecode.protobuf.format.JsonFormat.printToString;

public class IMSession implements Serializable {
    public static final int TYPE_APP = 0;
    public static final int TYPE_WEB = 1;
    public static final int DECODE_TYPE_PROBUF = 0;
    public static final int DECODE_TYPE_WEB_TEXT = 1;
    public static final int DECODE_TYPE_WEB_BINARY = 2;
    private static final Logger logger = LoggerFactory.getLogger(IMSession.class);
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
    //解码类型
    private int decodeType;

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

    public int getDecodeType() {
        return decodeType;
    }

    public void setDecodeType(int decodeType) {
        this.decodeType = decodeType;
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

    public boolean write(Message.Data msg) {
        if (channel != null && channel.isActive()) {
            if (decodeType == DECODE_TYPE_WEB_TEXT) {
                TextWebSocketFrame txframe = new TextWebSocketFrame(printToString(msg));
                channel.writeAndFlush(txframe).awaitUninterruptibly(5000);
            } else if (decodeType == DECODE_TYPE_WEB_BINARY) {
                ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.toByteArray());
                try {
                    Data.Builder builder = Data.newBuilder().mergeFrom(byteBuf.array());
                    String s = JsonFormat.printToString(builder.build());
                    logger.error("json =====" + s);
                } catch (Exception e) {
                    logger.error("json ==xxxxxx===");
                }
                BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
                channel.writeAndFlush(binaryWebSocketFrame).awaitUninterruptibly(5000);
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