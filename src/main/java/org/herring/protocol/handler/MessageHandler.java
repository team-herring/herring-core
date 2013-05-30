package org.herring.protocol.handler;

import org.herring.protocol.NetworkContext;

/**
 * @author Chiwan Park
 * @since 0.2
 */
public abstract class MessageHandler {

    /**
     * 네트워크를 통해 외부에서 데이터가 도착했을 때 호출되는 메서드. 주어지는 데이터는 이미 {@link org.herring.protocol.codec.HerringCodec}을 통해 디코딩이 이루어진 상태이다.
     *
     * @param context 해당 메시지가 도착한 NetworkContext
     * @param data    도착한 데이터
     * @throws Exception 여기서 발생되는 예외는 Logger에 기록된다.
     */
    public void messageArrived(NetworkContext context, Object data) throws Exception {}

    /**
     * Channel 연결이 성립되어 네트워크 통신이 가능한 상태에서 호출되는 메서드.
     *
     * @param context 통신 가능한 NetworkContext
     * @throws Exception 여기서 발생되는 예외는 Logger에 기록된다.
     */
    public void channelReady(NetworkContext context) throws Exception {}

    /**
     * Channel 연결이 어떤 이유에서든 종료되었을 때 호출되는 메서드.
     *
     * @param context 비정상적인 이유료 종료된 NetworkContext
     * @throws Exception 여기서 발생되는 예외는 Logger에 기록된다.
     */
    public void channelInactive(NetworkContext context) throws Exception {}

    /**
     * Channel 연결 종료를 요청하고, 정상적으로 종료되었을 때 호출되는 메서드.
     *
     * @param context 종료를 요청했던 NetworkContext
     * @throws Exception 여기서 발생되는 예외는 Logger에 기록된다.
     */
    public void channelClosed(NetworkContext context) throws Exception {}

    /**
     * 네트워크의 모든 연결 종료를 요청하고, 정상적으로 종료되었을 때 호출되는 메서드.
     *
     * @throws Exception 여기서 발생되는 예외는 Logger에 기록된다.
     */
    public void networkStopped() throws Exception {}

    /**
     * NetworkContext에서 sendObject를 요청하여 성공적으로 메시지를 전송했을 때 호출되는 메서드.
     *
     * @param context sendObject를 요청했던 NetworkContext
     * @throws Exception 여기서 발생되는 예외는 Logger에 기록된다.
     */
    public void sendComplete(NetworkContext context) throws Exception {}
}
