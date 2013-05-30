package org.herring.core.protocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.InternalDecoder;
import org.herring.core.protocol.codec.InternalEncoder;
import org.herring.core.protocol.handler.InternalHandler;
import org.herring.core.protocol.handler.MessageHandler;

/**
 * @author Chiwan Park
 * @since 0.2
 */
public class ComponentInitializer extends ChannelInitializer<SocketChannel> {

    private final HerringCodec codec;
    private final MessageHandler handler;

    public ComponentInitializer(HerringCodec codec, MessageHandler handler) {
        super();

        this.codec = codec;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("decoder", new InternalDecoder(codec));
        pipeline.addLast("encoder", new InternalEncoder(codec));

        pipeline.addLast("handler", new InternalHandler(handler));
    }
}
