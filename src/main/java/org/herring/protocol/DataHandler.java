package org.herring.protocol;

import java.io.IOException;

/**
 * 형태에 따라 데이터를 다루는 방법을 정의하는 인터페이스이다. 사실상의 프로토콜 정의 인터페이스라고 봐도 무방하다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public interface DataHandler<T> {

    /**
     * 데이터가 도착했을 때 수행되는 이벤트 리스너 메서드이다. 데이터가 온전히 도착했을 때만 호출됨이 보장되며, 일부분이 도착된 경우 버퍼에 자동적으로 대기시켜 온전히 도착할 때 까지 호출되지 않는다.
     *
     * @param ctx  네트워크에 접근하는 Context 객체
     * @param data 데이터를 담고 있는 객체
     */
    public void dataReceived(NetworkContext ctx, T data);

    /**
     * 연결이 성립되어 통신할 준비가 완료된 상태를 알리는 이벤트 리스너 메서드다.
     *
     * @param ctx 네트워크에 접근하는 Context 객체
     */
    public void connectionReady(NetworkContext ctx);

    /**
     * sendBytes가 수행되어 데이터 전송이 완료되었을 때 발생되는 이벤트 리스너 메서드다.
     *
     * @param ctx 네트워크에 접근하는 Context 객체
     */
    public void sendComplete(NetworkContext ctx);

    /**
     * 객체를 byte 데이터로 변환하는 메서드다.
     *
     * @param data 변환할 객체
     * @return 인코딩 된 byte 데이터
     * @throws IOException
     */
    public byte[] encode(T data) throws IOException;

    /**
     * byte 데이터를 프로토콜 정의에 따라 필요한 객체로 변환하는 메서드다.
     *
     * @param packet 전송된 byte 데이터
     * @return 프로토콜 정의에 따라 변환된 객체. 이 객체가 이벤트 리스너 메서드의 데이터 매개변수로 전달된다.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public T decode(byte[] packet) throws IOException, ClassNotFoundException;
}
