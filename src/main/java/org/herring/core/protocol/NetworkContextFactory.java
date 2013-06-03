package org.herring.core.protocol;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
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
