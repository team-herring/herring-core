package org.herring.core.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Herring Protocol Library 내부에서 Netty와 통신하기 위해 사용하는 ByteEncoder 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class InternalEncoder extends MessageToByteEncoder<Object> {

    private HerringCodec codec;

    public InternalEncoder(HerringCodec codec) {
        super();

        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] encodedPacket = codec.encode(msg);

        out.writeInt(encodedPacket.length);
        out.writeBytes(encodedPacket);
    }
}
