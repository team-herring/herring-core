package org.herring.core.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.herring.core.protocol.NetworkContext;
import org.herring.core.protocol.NetworkContextFactory;

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
    private static final NetworkContextFactory CONTEXT_FACTORY = NetworkContextFactory.getInstance();

    private MessageHandler handler;

    public InternalHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Channel Active");

        NetworkContext context = CONTEXT_FACTORY.getContext(ctx.channel());
        if (context.isAvailableLatch("active"))
            context.getLatch("active").countDown();

        if (handler != null)
            handler.channelActive(context);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Channel Inactive");

        NetworkContext context = CONTEXT_FACTORY.getContext(ctx.channel());
        if (context.isAvailableLatch("inactive"))
            context.getLatch("inactive").countDown();

        if (handler != null)
            handler.channelInactive(context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(Level.WARNING, "Exception Caught: " + cause.getMessage());

        cause.printStackTrace();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.log(Level.INFO, "Message Received");

        NetworkContext context = CONTEXT_FACTORY.getContext(ctx.channel());
        if (context.isAvailableLatch("received"))
            context.getLatch("received").countDown();

        if (handler != null) {
            if (!handler.messageArrived(context, msg))
                context.addMessageToQueue(msg);
        }
    }
}
