package org.herring.core.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.handler.MessageHandler;

/**
 * Herring Protocol Library를 사용하는 Client Network Component 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ClientComponent implements NetworkComponent {

    private final String host;
    private final int port;

    private final MessageHandler msgHandler;
    private final HerringCodec codec;

    private EventLoopGroup group;
    private NetworkContext networkContext;

    private Bootstrap bootstrap;

    private boolean active;

    public ClientComponent(String host, int port, HerringCodec codec, MessageHandler msgHandler) {
        this.host = host;
        this.port = port;
        this.msgHandler = msgHandler;
        this.codec = codec;
        this.active = false;

        initialize();
    }

    @Override
    public void initialize() {
        bootstrap = new Bootstrap();

        group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ComponentInitializer(codec, msgHandler));
    }

    @Override
    public void start() throws Exception {
        Channel nettyChannel = bootstrap.connect(host, port).syncUninterruptibly().channel();
        networkContext = NetworkContextFactory.getInstance().getContext(nettyChannel);

        active = true;
    }

    @Override
    public void stop() {
        networkContext.getChannel().close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                future.channel().eventLoop().shutdownGracefully();
                group.shutdownGracefully();

                active = false;
            }
        });
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public NetworkContext getNetworkContext() {
        return networkContext;
    }
}
