package org.herring.core.protocol;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Netty Channel 객체에서 Herring Framework에 필요한 기능만 제공하는 Wrapper 클래스. 모든 네트워크 핸들러에서 이 객체가 제공되며, 이 객체는 1회용이므로 반드시 핸들러 내부에서만 사용하는 것이 원칙이다.
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
     * Netty Channel을 직접 다룰일이 있을 때 이 메서드를 통해 객체를 접근한다. 그러나 이 메서드에서 {@link Channel} 객체를 받는 행위는 Herring Framework에 의해 통제되지 않으므로 권장되지 않는다.
     *
     * @return Netty Channel 객체
     * @throws IllegalStateException 현재 Netty Channel이 할당되지 않은 상태에서 발생
     */
    public Channel getChannel() throws IllegalStateException {
        if (channel == null)
            throw new IllegalStateException("Channel is not registered.");

        return channel;
    }

    public void setChannel(Channel channel) throws IllegalStateException {
        if (this.channel != null)
            throw new IllegalStateException("Channel is already registered.");

        this.channel = channel;
    }

    /**
     * 등록된 Netty Channel로 지정된 Object를 전송한다. 이 때 전송되는 Object는 반드시 알맞은 {@link org.herring.core.protocol.codec.HerringCodec}이 지정 네트워크에 등록되어있어야한다. 성공적으로 전송이 수행되면, 주어진 핸들러의 sendComplete 메서드가 호출된다.
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
     * NetworkContext 객체에 연결된 Channel을 닫는다. 이 작업은 즉시 이루어지지 않고 close 메서드는 즉시 리턴된다. close의 성공여부는 주어진 핸들러를 통해 통보된다.
     */
    public void close() {
        Channel channel = getChannel();

        if (channel.isOpen())
            channel.close();
    }

    /**
     * 현재 Channel에 연결된 원격지 컴퓨터의 주소를 반환한다.
     *
     * @return Channel에 연결된 원격지 컴퓨터의 주소
     */
    public String getRemoteAddress() {
        Channel channel = getChannel();

        return channel.remoteAddress().toString();
    }

    public void setLatch(String key, int count) {
        if (isAvailableLatch(key))
            throw new IllegalStateException("이미 Latch가 설정되어 있습니다.");
        if (latchMap.containsKey(key))
            latchMap.remove(key);

        latchMap.put(key, new CountDownLatch(count));
    }

    public void setLatch(String key) {
        setLatch(key, 1);
    }

    public CountDownLatch getLatch(String key) {
        if (!isAvailableLatch(key))
            throw new IllegalArgumentException("Latch 키가 잘못되었습니다.");

        return latchMap.get(key);
    }

    public boolean isAvailableLatch(String key) {
        return latchMap.containsKey(key) && latchMap.get(key).getCount() != 0;
    }

    public void waitUntil(String event, long timeout) throws InterruptedException {
        if (!isAvailableLatch(event))
            setLatch(event);

        getLatch(event).await(timeout, TimeUnit.MILLISECONDS);
    }

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

    public Object getMessageFromQueue() {
        if (receivedMessageQueue.isEmpty())
            throw new IllegalStateException("MessageQueue에 Message가 없습니다.");

        return receivedMessageQueue.poll();
    }
}
