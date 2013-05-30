package org.herring.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Chiwan Park
 * @since 0.2
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
