package org.herring.protocol.tests;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.herring.protocol.ClientComponent;
import org.herring.protocol.codec.HerringCodec;
import org.herring.protocol.codec.SerializableCodec;
import org.herring.protocol.handler.MessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Herring Protocol Framework를 활용한 Echo Client 예제이다. bye라는 문자열을 입력할 때 까지 연결을 계속 유지하며, 입력한 문자열을 Echo Server로 전송하고, 돌아오는 문자열을 화면에 출력해준다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class EchoClientSample {
    private final static String host = "127.0.0.1";
    private final static int port = 9928;

    private ClientComponent clientComponent;

    public static void main(String[] args) throws Exception {
        EchoClientSample clientSample = new EchoClientSample();

        MessageHandler handler = new MessageHandler() {
            @Override
            public void messageArrived(ChannelHandlerContext context, Object data) throws Exception {
                System.out.println((String) data);
            }

            @Override
            public void channelReady(ChannelHandlerContext context) throws Exception {
                System.out.println("연결 준비");
            }

            @Override
            public void channelBroken(ChannelHandlerContext context) throws Exception {
                System.out.println("연결 끊어짐");
            }

            @Override
            public void channelClosed(Channel channel) throws Exception {
                System.out.println("연결 종료됨");
            }
        };

        HerringCodec codec = new SerializableCodec();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            clientSample.clientComponent = new ClientComponent(host, port, codec, handler);
            clientSample.clientComponent.start();

            String input;

            do {
                input = in.readLine();
                clientSample.clientComponent.getChannel().write(input);
            } while (!"bye".equalsIgnoreCase(input));
        } finally {
            clientSample.clientComponent.stop();
        }
    }
}
