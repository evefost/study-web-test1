package com.big.data.service;

import com.big.data.vo.BaseMessage;
import io.netty.channel.ChannelHandlerContext;


public interface MessageService {

     void dispatcherMessage(ChannelHandlerContext ctx, Object msg);
}
