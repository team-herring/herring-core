package org.herring.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.herring.protocol.handler.MessageHandler;

/**
 * Netty Channel 객체에서 Herring Framework에 필요한 기능만 제공하는 Wrapper 클래스. 모든 네트워크 핸들러에서 이 객체가 제공되며, 이 객체는 1회용이므로 반드시 핸들러 내부에서만 사용하는 것이 원칙이다.
 *
 * @author Chiwan Park
 * @since 0.2
 */
public class NetworkContext {

    private Channel channel;

    public NetworkContext() {
        channel = null;
    }

    public NetworkContext(Channel channel) {
        this();

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
     * 등록된 Netty Channel로 지정된 Object를 전송한다. 이 때 전송되는 Object는 반드시 {@link org.herring.protocol.codec.HerringCodec}이 구현되어있어야하며, 해당 Codec이 지정 네트워크에 등록되어있어야한다. 성공적으로 전송이 수행되면, 주어진 핸들러의 sendComplete 메서드가 호출된다.
     *
     * @param object  전송할 객체
     * @param handler 결과를 통보받을 핸들러
     * @throws IllegalStateException Channel이 부적절한 상태인 경우 발생한다.
     */
    public void sendObject(Object object, final MessageHandler handler) throws IllegalStateException {
        Channel channel = getChannel();

        if (!channel.isActive())
            throw new IllegalStateException("Channel is not active.");

        channel.write(object);
        channel.flush().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                handler.sendComplete(new NetworkContext(future.channel()));
            }
        });
    }

    /**
     * NetworkContext 객체에 연결된 Channel을 닫는다. 이 작업은 즉시 이루어지지 않고 close 메서드는 즉시 리턴된다. close의 성공여부는 주어진 핸들러를 통해 통보된다.
     *
     * @param handler 결과를 통보받을 핸들러
     */
    public void close(final MessageHandler handler) {
        Channel channel = getChannel();

        if (channel.isOpen()) {
            channel.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    handler.channelClosed(new NetworkContext(future.channel()));
                }
            });
        }
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
}
