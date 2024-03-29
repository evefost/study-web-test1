package com.big.data.service;

import com.im.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;


public interface MessageService {

     void addChannel(int type, SocketChannel channel);

     void dispatcherMessage(ChannelHandlerContext ctx, Object msg);

     void broadCast(Message.Data data);
}
