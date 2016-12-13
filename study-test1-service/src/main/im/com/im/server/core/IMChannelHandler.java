/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.im.server.core;

import com.im.sdk.protocol.Message;
import com.im.sdk.protocol.Message.Data;
import com.im.sdk.protocol.Message.Data.Cmd;
import com.im.server.util.ProtocolHandllerLoader;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class IMChannelHandler extends SimpleChannelInboundHandler<Message.Data> {
    private static final Logger logger = LoggerFactory.getLogger(IMChannelHandler.class);
    /**
     * Creates a client-side handler.
     */
    public IMChannelHandler() {

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Data data) throws Exception {
        showMessageInfoLog(ctx, data);
        ProtocolHandler handler = ProtocolHandllerLoader.getProtocolHandler(data.getCmd());
        logger.info("messageReceived content :" + data.getContent());
        Set<Map.Entry<String, SocketChannel>> entries = IMServer.ches.entrySet();
        for (Map.Entry<String, SocketChannel> entry : entries) {
            entry.getValue().write(new TextWebSocketFrame(data.getContent()));
            entry.getValue().flush();
        }
        if (handler != null) {
            handler.handleRequest(ctx, data);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        logger.debug("channelActive 已连上服务器 发送聊天服务地址给客户端 :" + ctx.channel().remoteAddress());
        logger.debug("channelId :" + ctx.channel().id());

    }



    private void showMessageInfoLog(ChannelHandlerContext ctx, Message.Data data) {
        switch (data.getCmd()) {
            case Cmd.LOGIN_VALUE:
                logger.debug("channelRead 登录消息 :" + ctx.channel().remoteAddress());
                break;
            case Cmd.LOGOUT_VALUE:
                logger.debug("channelRead 登出消息 :" + ctx.channel().remoteAddress());
                break;
            case Cmd.CHAT_TXT_VALUE:
                // logger.info("channelRead
                // 普通消息:"+data.getContent()+"==time:"+data.getCreateTime());
                break;
            case Cmd.HEARTBEAT_VALUE:
                logger.debug("channelRead  心跳消息:");
                break;
            case Cmd.BIND_CLIENT_VALUE:
                logger.debug("channelRead  绑定clientId:" + data.getClientId());
                break;
            default:
                break;
        }
    }

    /**
     * 检测到连接空闲事件，发送心跳请求命令
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //TODO
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("exceptionCaught 异常关闭");
        cause.printStackTrace();
        ctx.close();
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("channelInactive 客户端断开:" + ctx.channel().remoteAddress());
    }
}
