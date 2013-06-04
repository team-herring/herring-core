package org.herring.core.zookeeper;

/**
 * Herring Zookeeper Library에서 사용하는 예외 기본 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ZookeeperException extends Exception {

    private static final long serialVersionUID = -2085970797064807447L;

    public ZookeeperException() {
        super();
    }

    public ZookeeperException(String message) {
        super("zkClient: " + message);
    }
}
