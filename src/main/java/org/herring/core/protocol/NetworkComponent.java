package org.herring.core.protocol;

/**
 * Herring Network를 구성하는 구성요소 인터페이스.
 * 모든 Herring Network Component는 이 인터페이스를 구현해야한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public interface NetworkComponent {
    /**
     * Netty 라이브러리를 각각의 용도에 맞게 초기화하고 구성요소가 실행될 준비를 수행하는 메서드.
     * 이 메서드는 반드시 @{code start} 메서드보다 먼저 수행되어야하며, 가급적 생성자에서 호출하는 것이 좋다.
     */
    public void initialize();

    /**
     * 실제로 구성요소를 작동시키는 메서드.
     * 이 메서드가 실행되면서 네트워크 통신이 발생한다. 이 메서드는 통신이 가능한 상태가 될 때 까지 기다린다.
     */
    public void start() throws Exception;

    /**
     * 작동중인 네트워크를 중단하는 메서드.
     * 이 메서드를 호출하면 수행중인 모든 네트워크 채널을 닫고 종료 상태로 돌아간다. 이 메서드는 네트워크의 종료를 기다리지 않는다.
     */
    public void stop();

    /**
     * 현재 작동중인 상태인지 확인하는 메서드.
     *
     * @return 현재 Component가 작동중이면 true, 아니면 false
     */
    public boolean isActive();
}
