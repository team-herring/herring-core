package org.herring.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 서버 역할을 수행하는 구성요소 클래스. 지정 포트를 열고 대기하여 클라이언트로 부터 응답이 오면 작업을 수행한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ServerComponent implements NetworkComponent {
    private final int port;
    private final RawPacketHandler handler;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    public ServerComponent(int port, DataHandler handler) {
        this.port = port;
        this.handler = new RawPacketHandler(handler);

        configureNetty();
    }

    public void configureNetty() {
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
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
        channelFuture = bootstrap.bind(port).syncUninterruptibly();
    }

    public void stop() {
        // TODO: 추후 이 부분도 비동기 처리가 되도록 구조 변경 필요.
        if (channelFuture.channel().isOpen())
            channelFuture.channel().closeFuture().syncUninterruptibly();
    }

    public void destroy() {
        stop();

        if (!bossGroup.isShutdown())
            bossGroup.shutdown();

        if (!workerGroup.isShutdown())
            workerGroup.shutdown();

        if (bootstrap != null) {
            bootstrap.shutdown();
            bootstrap = null;
        }
    }
}
