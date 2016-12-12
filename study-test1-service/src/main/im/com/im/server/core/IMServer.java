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

import com.big.data.socket.server.WebSocketServerHandler;
import com.im.sdk.protocol.Message;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

//import com.im.sdk.protocol.ProtobufDecoder;
//import com.im.sdk.protocol.ProtobufEncoder;

public final class IMServer {


    private int port;

    public void start() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("=====================>");
                System.out.println("=====================>");
                System.out.println("=====================>");
                System.out.println("=====================>");
                System.out.println("ServerBootstrap 启动");
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
                            System.out.println("新连接=======remoteAddress>" + ch.remoteAddress());
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ProtobufDecoder(Message.Data.getDefaultInstance()));
                            p.addLast(new ProtobufEncoder());
                            p.addLast(new IMChannelHandler());
                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpObjectAggregator(65536));
                            p.addLast(new WebSocketServerCompressionHandler());
                            p.addLast(new WebSocketServerHandler());
                        }
                    });

                    bootstrap.bind(port).sync().channel().closeFuture().sync();
                    System.out.println("绑定成功:");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("绑定异常bindEx:" + e.toString());
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
