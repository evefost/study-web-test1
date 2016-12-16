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

import com.big.data.service.MessageService;
import com.im.protocol.Message;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public final class IMServer {
    private static final Logger logger = LoggerFactory.getLogger(IMServer.class);
    @Autowired
    MessageService messageService;
    @Value("${socket.port}")
    private int port;

    public void start() {

        new Thread(new Runnable() {

            public void run() {
                logger.info("=====================>");
                logger.info("ServerBootstrap 启动");
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
                            logger.debug("新连接=======id>" + ch.id());
                            messageService.addChannel(0, ch);
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProtobufDecoder(Message.Data.getDefaultInstance()));
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new IMChannelHandler(messageService));
                        }
                    });

                    bootstrap.bind(port).sync().channel().closeFuture().sync();
                    System.out.println("绑定成功:");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("绑定异常bindEx:" + e.toString());
                } finally {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }
            }
        }).start();
    }

    public void setPort(int port) {
        this.port = port;
    }
}
