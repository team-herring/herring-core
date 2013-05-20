package org.herring.protocol;

/**
 * Herring Network를 구성하는 구성요소 인터페이스. 모든 Herring Network Component는 이 인터페이스를 구현해야한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public interface NetworkComponent {
    /**
     * Netty 라이브러리를 각각의 용도에 맞게 초기화하고 구성요소가 실행될 준비를 수행하는 메서드다.
     */
    public void configureNetty();

    /**
     * 실제로 구성요소를 작동시키는 메서드다. 이 메서드가 실행되면서 네트워크 통신이 발생한다.
     */
    public void start();

    /**
     * 작동중인 네트워크를 중단하는 메서드다. 이 메서드를 호출하면 수행중인 네트워크 채널을 닫고 종료 상태로 돌아간다. 이 메서드는 적절한 방법으로 소켓이 닫힐 때 까지 기다린다.
     */
    public void stop();

    /**
     * 네트워크를 중단하고, 기존 설정을 제거하는 메서드다. 이 메서드를 호출하면 수행중인 네트워크가 즉시 종료되는 것은 물론, 기존의 설정들도 전부 소멸된다.
     */
    public void destroy();
}
