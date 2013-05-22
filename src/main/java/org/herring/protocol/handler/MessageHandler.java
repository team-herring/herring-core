package org.herring.protocol.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Chiwan Park
 * @since 0.2
 */
public interface MessageHandler {

    public void messageArrived(ChannelHandlerContext context, Object data) throws Exception;

    public void channelReady(ChannelHandlerContext context) throws Exception;

    public void channelBroken(ChannelHandlerContext context) throws Exception;

    public void channelClosed(Channel channel) throws Exception;
}
