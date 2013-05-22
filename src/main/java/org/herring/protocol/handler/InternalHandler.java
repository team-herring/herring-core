package org.herring.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Netty에서 들어오는 데이터를 맨 처음 다루는 Handler 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class InternalHandler extends ChannelInboundMessageHandlerAdapter<Object> {

    private static final Logger logger = Logger.getLogger(InternalHandler.class.getName());

    private MessageHandler handler;

    public InternalHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Channel Active");

        handler.channelReady(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Channel Inactive");

        handler.channelBroken(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(Level.WARNING, "Exception Caught: " + cause.getMessage());

        cause.printStackTrace();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.log(Level.INFO, "Message Received");

        handler.messageArrived(ctx, msg);
    }
}
