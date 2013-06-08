package org.herring.core.protocol;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Netty Channel 객체에서 Herring Framework에 필요한 기능만 제공하는 Wrapper 클래스.
 * 이 객체는 Netty Channel이 적법하게 존재하는 한, 반드시 존재한다.
 *
 * @author Chiwan Park
 * @since 0.2
 */
public class NetworkContext {

    private Channel channel;
    private Map<String, CountDownLatch> latchMap;
    private Queue<Object> receivedMessageQueue;

    public NetworkContext() {
        channel = null;
    }

    public NetworkContext(Channel channel) {
        this();
        latchMap = new ConcurrentHashMap<String, CountDownLatch>();
        receivedMessageQueue = new LinkedBlockingQueue<Object>();

        setChannel(channel);
    }

    /**
     * Netty Channel에 직접 접근하여 돌려주는 메서드.
     * Netty Channel을 직접 다룰 일이 있을 때 사용한다. 그러나 이 메서드에서 {@link Channel} 객체를 받는 행위는 Herring Core Framework에 의해 통제되지 않으므로 권장되지 않는다.
     *
     * @return Netty Channel 객체
     * @throws IllegalStateException 현재 Netty Channel이 할당되지 않은 상태에서 발생
     */
    public Channel getChannel() throws IllegalStateException {
        if (channel == null)
            throw new IllegalStateException("Channel is not registered.");

        return channel;
    }

    /**
     * Netty Channel을 다른 채널로 변경하는 메서드.
     * NetworkContext에 연결된 Netty Channel을 새로이 설정할 때 사용한다. 그러나 이 메서드에서 @{code Channel}를 변경할 경우, NetworkContext의 일관성이 깨지게 되므로 권장되지 않는다.
     *
     * @param channel 새로이 갱신할 Netty Channel 객체
     * @throws IllegalStateException 이미 연결된 Netty Channel이 존재할 때 발생한다.
     */
    public void setChannel(Channel channel) throws IllegalStateException {
        if (this.channel != null)
            throw new IllegalStateException("Channel is already registered.");

        this.channel = channel;
    }

    /**
     * 등록된 Netty Channel로 지정된 Object를 전송하는 메서드.
     * 이 때 전송되는 Object는 반드시 알맞은 {@link org.herring.core.protocol.codec.HerringCodec}이 지정 네트워크에 등록되어있어야한다.
     *
     * @param object 전송할 객체
     * @throws IllegalStateException Channel이 부적절한 상태인 경우 발생한다.
     */
    public void sendObject(Object object) throws IllegalStateException {
        Channel channel = getChannel();

        if (!channel.isActive())
            throw new IllegalStateException("Channel is not active.");

        channel.write(object);
        channel.flush();
    }

    /**
     * NetworkContext 객체에 연결된 Channel을 닫는 메서드.
     * 이 작업은 즉시 이루어지지 않고 close 메서드는 즉시 리턴된다.
     */
    public void close() {
        Channel channel = getChannel();

        if (channel.isOpen())
            channel.close();
    }

    /**
     * 현재 Channel에 연결된 원격지 컴퓨터의 주소를 반환하는 메서드.
     *
     * @return Channel에 연결된 원격지 컴퓨터의 주소
     */
    public String getRemoteAddress() {
        Channel channel = getChannel();

        return channel.remoteAddress().toString();
    }

    /**
     * NetworkContext에 연결된 스레드의 작동을 관리하기 위해 CountDownLatch를 생성하는 메서드.
     *
     * @param key   Latch를 식별할 Key String. 동일 Key String은 같은 Latch에 접근하게 해준다.
     * @param count Latch의 Threshold Count.
     */
    public void setLatch(String key, int count) {
        if (isAvailableLatch(key))
            throw new IllegalStateException("이미 Latch가 설정되어 있습니다.");
        if (latchMap.containsKey(key))
            latchMap.remove(key);

        latchMap.put(key, new CountDownLatch(count));
    }

    /**
     * NetworkContext에 연결된 스레드의 작동을 관리하기 위해 CountDownLatch를 생성하는 메서드.
     * 기본적으로 하나의 스레드를 관리하도록 Latch Count를 1로 설정한다.
     *
     * @param key Latch를 식별할 Key String. 동일 Key String은 같은 Latch에 접근하게 해준다.
     */
    public void setLatch(String key) {
        setLatch(key, 1);
    }

    /**
     * Key String에 연결된 CountDownLatch를 가져오는 메서드.
     *
     * @param key 가져올 Latch의 Key String
     * @return 해당 Key에 연결된 CountDownLatch 객체
     */
    public CountDownLatch getLatch(String key) {
        if (!isAvailableLatch(key))
            throw new IllegalArgumentException("Latch 키가 잘못되었습니다.");

        return latchMap.get(key);
    }

    /**
     * Key String에 연결된 CountDownLatch가 유효한지 확인하는 메서드.
     *
     * @param key 확인할 Latch의 Key String
     * @return 만약 유효하면 true, 아니면 false
     */
    public boolean isAvailableLatch(String key) {
        return latchMap.containsKey(key) && latchMap.get(key).getCount() != 0;
    }

    /**
     * Key String에 연결된 Latch가 전부 종료될 때 까지 기다리는 메서드.
     *
     * @param event   대기할 Latch의 Key String
     * @param timeout 최대 대기 시간 (milliseconds)
     * @throws InterruptedException 중간에 인터럽트가 발생했을 때
     */
    public void waitUntil(String event, long timeout) throws InterruptedException {
        if (!isAvailableLatch(event))
            setLatch(event);

        getLatch(event).await(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Key String에 연결된 Latch가 전부 종료될 때 까지 기다리는 메서드.
     * 시간 제한 없이 무제한 기다린다.
     *
     * @param event 대기할 Latch의 Key String
     * @throws InterruptedException 중간에 인터럽트가 발생했을 때
     */
    public void waitUntil(String event) throws InterruptedException {
        if (!isAvailableLatch(event))
            setLatch(event);

        getLatch(event).await();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NetworkContext && getChannel().equals(((NetworkContext) obj).getChannel());
    }

    public void addMessageToQueue(Object o) {
        receivedMessageQueue.add(o);
    }

    /**
     * 현재 Queue에 들어있는 도착한 Message를 받아오는 메서드.
     *
     * @return 가장 먼저 도착한 Message
     */
    public Object getMessageFromQueue() {
        if (receivedMessageQueue.isEmpty())
            throw new IllegalStateException("MessageQueue에 Message가 없습니다.");

        return receivedMessageQueue.poll();
    }
}
