package org.herring.core.protocol.samples;

import org.herring.core.protocol.ClientComponent;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.herring.core.protocol.handler.MessageHandler;
import org.herring.core.protocol.handler.SyncMessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 동기 통신을 실험하기 위한 샘플 클라이언트 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class IntervalClientSample {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9939;

    private ClientComponent clientComponent;

    public static void main(String[] args) throws Exception {
        IntervalClientSample clientSample = new IntervalClientSample();
        HerringCodec codec = new SerializableCodec();

        // 동기 통신을 위한 기본 핸들러. 비동기 통신 핸들러라 하더라도 받은 메시지에 따라 리턴 값을 true, false로 조정하여 동기, 비동기 통신을 이어나갈 수 있다.
        MessageHandler messageHandler = new SyncMessageHandler();

        clientSample.clientComponent = new ClientComponent(HOST, PORT, codec, messageHandler);
        clientSample.clientComponent.start(); // 클라이언트 시작

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        while (!"go".equalsIgnoreCase(input.readLine())) ; // 사용자 명령 대기

        clientSample.clientComponent.getNetworkContext().sendObject("hello server!"); // 서버에 메시지 요청
        clientSample.clientComponent.getNetworkContext().waitUntil("received"); // 메시지 도착할 때까지 대기

        System.out.println(clientSample.clientComponent.getNetworkContext().getMessageFromQueue()); // 도착 메시지 출력

        clientSample.clientComponent.stop(); // 클라이언트 종료
    }
}
