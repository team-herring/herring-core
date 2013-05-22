package org.herring.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.herring.protocol.codec.HerringCodec;
import org.herring.protocol.handler.MessageHandler;

/**
 * 서버 역할을 수행하는 구성요소 클래스. 지정 포트를 열고 대기하여 클라이언트로 부터 응답이 오면 작업을 수행한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ServerComponent implements NetworkComponent {
    private final int port;
    private final MessageHandler msgHandler;
    private final HerringCodec codec;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;

    private Channel nettyChannel;

    public ServerComponent(int port, HerringCodec codec, MessageHandler msgHandler) {
        this.port = port;
        this.msgHandler = msgHandler;
        this.codec = codec;

        initialize();
    }

    @Override
    public void initialize() {
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childHandler(new ComponentInitializer(codec, msgHandler));
    }

    @Override
    public void start() throws Exception {
        nettyChannel = bootstrap.bind(port).syncUninterruptibly().channel();
    }

    @Override
    public void stop() {
        nettyChannel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                future.channel().eventLoop().shutdownGracefully();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();

                msgHandler.channelClosed(future.channel());
            }
        });
    }
}
