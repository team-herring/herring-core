package org.herring.protocol.tests;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.herring.protocol.ServerComponent;
import org.herring.protocol.codec.HerringCodec;
import org.herring.protocol.codec.SerializableCodec;
import org.herring.protocol.handler.MessageHandler;

/**
 * Herring Protocol Framework를 활용한 Echo Server 예제이다. Herring Client에서 전송된 내용을 그대로 돌려보내준다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class EchoServerSample {

    private final static int port = 9928;

    private ServerComponent serverComponent;

    public static void main(String[] args) throws Exception {
        EchoServerSample serverInstance = new EchoServerSample();

        HerringCodec codec = new SerializableCodec();

        MessageHandler handler = new MessageHandler() {
            @Override
            public void messageArrived(ChannelHandlerContext context, Object data) throws Exception {
                if ("bye".equalsIgnoreCase((String) data)) {
                    context.close().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            future.channel().eventLoop().shutdownGracefully();
                        }
                    });
                    return;
                }

                context.write(data);
            }

            @Override
            public void channelReady(ChannelHandlerContext context) throws Exception {
                System.out.println("연결 준비: " + context.channel().remoteAddress());
            }

            @Override
            public void channelBroken(ChannelHandlerContext context) throws Exception {
                System.out.println("연결 끊어짐: " + context.channel().remoteAddress());
            }

            @Override
            public void channelClosed(Channel channel) throws Exception {
                System.out.println("연결 종료됨: " + channel.remoteAddress());
            }
        };

        serverInstance.serverComponent = new ServerComponent(port, codec, handler);

        serverInstance.serverComponent.start();
    }
}
