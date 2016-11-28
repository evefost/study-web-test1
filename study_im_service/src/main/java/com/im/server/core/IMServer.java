package com.im.server.core;
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

import com.im.sdk.protocol.Message;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.im.sdk.protocol.ProtobufDecoder;
//import com.im.sdk.protocol.ProtobufEncoder;

public final class IMServer {
    private final Logger logger = LoggerFactory.getLogger(IMServer.class);


    private int port;

    public void bind() {

        logger.warn("ServerBootstrap 启动......");
        System.out.println("ServerBootstrap 启动......");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 码创建 ServerBootstrap 实例
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            // .添加 EchoServerHandler 到 Channel 的 ChannelPipeline
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    logger.info("新连接=======remoteAddress>" + ch.remoteAddress());
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new ProtobufDecoder(Message.Data.getDefaultInstance()));
                    p.addLast(new ProtobufEncoder());
                    p.addLast(new IMChannelHandler());
                }
            });

            bootstrap.bind(port).sync().channel().closeFuture().sync();
            logger.warn("绑定成功 port:" + port);
            System.out.println("绑定成功 port:" + port);
        } catch (Exception e) {
            logger.error("绑定异常bindEx:" + e.toString());
            System.out.println("绑定异常bindEx:" + e.toString());
        } finally {
            logger.error("绑定finally");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
//
    }

    public void setPort(int port) {
        this.port = port;
    }
}
