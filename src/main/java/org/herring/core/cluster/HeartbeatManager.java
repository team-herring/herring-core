package org.herring.core.cluster;

import org.herring.core.cluster.zookeeper.ZookeeperClient;

import java.io.IOException;
import java.util.UUID;

/**
 * Herring Cluster의 Heartbeat를 관리하는 클래스. 모든 Herring Cluster는 HeartbeatManager 객체를 가져야 하며,
 * {@link HeartbeatManager} 인스턴스의 startHeartbeat 메서드를 실행시켜 Cluster를 등록해야 한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class HeartbeatManager {

    private ClusterSharedStorage heartbeatStorage;

    private UUID uuid;
    private String type;

    // Heartbeat 데이터는 Herring System에 참여하는 모든 클러스터가 공유해야하는 자료이므로 Cluster Storage의 UUID를 동일하게 한다.
    private static final UUID HEARTBEAT_UUID = UUID.fromString("2E0E1AD0-14D5-4FB8-BF79-C31125AEFA8B");

    public HeartbeatManager(ZookeeperClient zkClient, UUID uuid, String type) {
        this.heartbeatStorage = new ClusterSharedStorage(zkClient, HEARTBEAT_UUID);
        this.uuid = uuid;
        this.type = type;
    }

    /**
     * Zookeeper와 연결을 시도하고, Heartbeat 유지를 시작한다.
     */
    public void startHeartbeat() throws IOException {
        createHeartbeatFile();
    }

    /**
     * Heartbeat 유지를 멈추고, Zookeeper 서버와의 연결을 종료한다.
     */
    public void stopHeartbeat() {
        deleteHeartbeatFile();
    }

    private void createHeartbeatFile() {
        heartbeatStorage.put(uuid.toString(), type);
    }

    private void deleteHeartbeatFile() {
        heartbeatStorage.remove(uuid.toString());
    }

    /**
     * 주어진 UUID의 Cluster가 현재 Cluster List의 등록이 된 바른 Cluster인지 확인한다.
     *
     * @param uuid 확인할 Cluster의 UUID.
     * @return 올바른 Cluster이면 true, 아니라면 false를 돌려준다.
     */
    public boolean isValidCluster(UUID uuid) {
        return heartbeatStorage.containsKey(uuid.toString());
    }

    /**
     * 주어진 UUID의 Cluster가 어떤 Type의 Cluster인지 확인한다.
     *
     * @param uuid 확인할 Cluster의 UUID.
     * @return 해당 Cluster의 Type.
     */
    public String getClusterType(UUID uuid) {
        if (!isValidCluster(uuid))
            throw new IllegalArgumentException("Cluster UUID is not valid.");

        return heartbeatStorage.get(uuid.toString());
    }
}
