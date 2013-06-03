package org.herring.core.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Chiwan Park
 * @since 0.2
 */
public class InternalDecoder extends LengthFieldBasedFrameDecoder {

    private HerringCodec codec;

    public InternalDecoder(HerringCodec codec) {
        // TODO: 프레임 길이를 적절하게 조정할 필요가 있음
        super(10485760, 0, 4, 0, 4);

        this.codec = codec;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf packetFrame = (ByteBuf) super.decode(ctx, in);
        if (packetFrame == null)
            return null;

        int packetLength = packetFrame.readableBytes();
        byte[] rawPacket = new byte[packetLength];

        packetFrame.readBytes(rawPacket, 0, packetLength);

        return codec.decode(rawPacket);
    }

    @Override
    protected ByteBuf extractFrame(ByteBuf buffer, int index, int length) {
        return buffer.slice(index, length);
    }
}
