package org.herring.core.protocol.samples;

import org.herring.core.protocol.ClientComponent;
import org.herring.core.protocol.NetworkContext;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.herring.core.protocol.handler.AsyncMessageHandler;
import org.herring.core.protocol.handler.MessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Herring Protocol Framework를 활용한 Echo Client 예제이다. bye라는 문자열을 입력할 때 까지 연결을 계속 유지하며, 입력한 문자열을 Echo Server로 전송하고, 돌아오는 문자열을 화면에 출력해준다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class EchoClientSample {
    private final static String HOST = "127.0.0.1";
    private final static int PORT = 9928;

    private ClientComponent clientComponent;

    public static void main(String[] args) throws Exception {
        EchoClientSample clientSample = new EchoClientSample();

        MessageHandler handler = new AsyncMessageHandler() {
            @Override
            public boolean messageArrived(NetworkContext context, Object data) throws Exception {
                System.out.println((String) data);
                System.out.println("Received Thread ID: " + Thread.currentThread().getId());

                return true;
            }
        };

        HerringCodec codec = new SerializableCodec();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            clientSample.clientComponent = new ClientComponent(HOST, PORT, codec, handler);
            clientSample.clientComponent.start();

            System.out.println("Main Thread ID: " + Thread.currentThread().getId());

            String input;
            do {
                input = in.readLine();
                clientSample.clientComponent.getNetworkContext().sendObject(input);
            } while (!"bye".equalsIgnoreCase(input));
        } finally {
            clientSample.clientComponent.stop();
        }
    }
}
