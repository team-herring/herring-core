package org.herring.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * Herring Protocol Handler에서 Network를 다루기 위해 제공하는 Context 객체. Netty의 HandlerContext를 그냥 제공할 경우, Netty -> Herring -> Custom 형태로 전송되는 규칙을 깨버리게 되어 제공한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class NetworkContext {
    private ChannelHandlerContext nettyContext;
    private RawPacketHandler packetHandler;

    public NetworkContext(ChannelHandlerContext nettyContext, RawPacketHandler packetHandler) {
        this.nettyContext = nettyContext;
        this.packetHandler = packetHandler;
    }

    /**
     * Object를 보내는 메서드다. 미리 지정된 Handler로 byte[] 형태로 인코딩하여 전송한다.
     *
     * @param data 보낼 객체
     * @throws IOException
     */
    public void sendObject(Object data) throws IOException {
        byte[] encoded = packetHandler.getDataHandler().encode(data);

        sendBytes(encoded);
    }

    /**
     * byte 데이터를 보내는 메서드다.
     *
     * @param data 보내려는 byte 데이터
     */
    public void sendBytes(byte[] data) {
        ByteBuf buf = nettyContext.nextOutboundByteBuffer();

        buf.writeInt(data.length);
        buf.writeBytes(data);

        nettyContext.flush().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                packetHandler.getDataHandler().sendComplete(new NetworkContext(nettyContext, packetHandler));
            }
        });
    }

    public void close() {
        nettyContext.channel().closeFuture().syncUninterruptibly();
    }
}
