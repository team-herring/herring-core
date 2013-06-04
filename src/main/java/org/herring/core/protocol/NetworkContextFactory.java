package org.herring.core.protocol;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NetworkContext 객체의 생성을 전담하는 클래스.
 * 모든 NetworkContext 객체는 Herring Protocol Library가 관리하기 위하여 NetworkContextFactory를 통해 생성되어야 한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class NetworkContextFactory {

    private Map<Integer, NetworkContext> map;

    private NetworkContextFactory() {
        map = new ConcurrentHashMap<Integer, NetworkContext>();
    }

    private static final class NetworkContextFactoryHolder {
        private static final NetworkContextFactory INSTANCE = new NetworkContextFactory();
    }

    public static NetworkContextFactory getInstance() {
        return NetworkContextFactoryHolder.INSTANCE;
    }

    public NetworkContext getContext(Channel channel) {
        int id = channel.id();

        if (!map.containsKey(id)) {
            map.put(id, new NetworkContext(channel));
        }

        return map.get(id);
    }
}
