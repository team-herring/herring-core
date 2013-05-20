package org.herring.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

import java.util.logging.Logger;

/**
 * Netty에서 들어오는 데이터를 맨 처음 다루는 Handler 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class RawPacketHandler extends ChannelInboundByteHandlerAdapter {
    private Logger logger = Logger.getLogger(RawPacketHandler.class.getName());

    private int packetLength = -1;
    private DataHandler dataHandler;

    public RawPacketHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    @Override
    protected void inboundBufferUpdated(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (packetLength == -1) {
            if (in.readableBytes() >= 4) {
                packetLength = in.readInt();
            }
        } else {
            if (in.readableBytes() >= packetLength) {
                byte[] packet = in.readBytes(packetLength).array();

                Object decoded = dataHandler.decode(packet);
                dataHandler.dataReceived(new NetworkContext(ctx, this), decoded);

                packetLength = -1;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        dataHandler.connectionReady(new NetworkContext(ctx, this));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.severe(cause.getMessage());
    }
}
