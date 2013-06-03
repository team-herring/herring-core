package org.herring.core.protocol.samples;

import org.herring.core.protocol.NetworkContext;
import org.herring.core.protocol.ServerComponent;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.herring.core.protocol.handler.AsyncMessageHandler;
import org.herring.core.protocol.handler.MessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 클라이언트 동기 통신을 실험하기 위한 간단한 샘플 서버 클래스. 실제 이 서버는 비동기 통신 메커니즘을 사용하여 통신한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class IntervalServerSample {

    private static final int port = 9939;

    private ServerComponent serverComponent;

    public static void main(String[] args) throws Exception {
        final IntervalServerSample serverSample = new IntervalServerSample();
        HerringCodec codec = new SerializableCodec();

        MessageHandler messageHandler = new AsyncMessageHandler() {
            @Override
            public boolean messageArrived(NetworkContext context, Object data) throws Exception {
                context.sendObject("hello client!");
                return true; // 비동기 통신이므로 현재 핸들러에서 메시지를 사용했음을 알린다.
            }
        };

        serverSample.serverComponent = new ServerComponent(port, codec, messageHandler);
        serverSample.serverComponent.start();

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        while (!"bye".equalsIgnoreCase(input.readLine())) ; // 서버 종료를 위한 사용자 입력 대기 (통신과는 무관하다)

        serverSample.serverComponent.stop();
    }
}
