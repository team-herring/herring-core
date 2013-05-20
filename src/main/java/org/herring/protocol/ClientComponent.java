package org.herring.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Herring Protocol Library를 사용하는 Client Network Component 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ClientComponent implements NetworkComponent {

    private final String host;
    private final int port;
    private final RawPacketHandler handler;

    private EventLoopGroup group;
    private ChannelFuture channelFuture;

    private Bootstrap bootstrap;

    public ClientComponent(String host, int port, DataHandler handler) {
        this.host = host;
        this.port = port;
        this.handler = new RawPacketHandler(handler);

        configureNetty();
    }

    public void configureNetty() {
        bootstrap = new Bootstrap();

        group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(handler);
                    }
                });
    }

    public void start() {
        // TODO: 추후 이 부분도 비동기 처리가 되도록 구조 변경 필요.
        channelFuture = bootstrap.connect(host, port).syncUninterruptibly();
    }

    public void stop() {
        // TODO: 추후 이 부분도 비동기 처리가 되도록 구조 변경 필요.
        if (channelFuture.channel().isOpen())
            channelFuture.channel().closeFuture().syncUninterruptibly();
    }

    public void destroy() {
        if (!group.isShutdown())
            group.shutdown();

        if (bootstrap != null) {
            bootstrap.shutdown();
            bootstrap = null;
        }
    }
}
