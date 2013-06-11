package org.herring.core.cluster.zookeeper;

/**
 * 이 객체는 @{link ZookeeperClient.addEventListener} 메서드를 통해 통지 받을 이벤트 리스너다.
 * 각각의 리스너 메서드에서 true를 리턴하면, 이벤트 리스너를 유지하고, false를 리턴하면 이벤트 리스너 유지를 멈춘다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ZookeeperWatchListener {

    public boolean childrenNodeChanged(String path) {
        return true;
    }

    public boolean nodeDataChanged(String path) {
        return true;
    }

    public void nodeDeleted(String path) {}
}
