package org.herring.core.cluster;

import org.herring.core.zookeeper.ZookeeperClient;
import org.herring.core.zookeeper.ZookeeperException;

import java.util.UUID;

/**
 * Herring Cluster의 Heartbeat를 관리하는 클래스. 모든 Herring Cluster는 HeartbeatManager 객체를 가져야 하며,
 * {@link HeartbeatManager} 인스턴스의 startHeartbeat 메서드를 실행시켜 Cluster를 등록해야 한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class HeartbeatManager {

    private ZookeeperClient zkClient;

    private UUID uuid;
    private String type;

    private static final String HEARTBEAT_PATH = "/herring/heartbeat/";

    private HeartbeatManager() {
        zkClient = null;
    }

    private static class HeartbeatManagerHolder {
        public static final HeartbeatManager INSTANCE = new HeartbeatManager();
    }

    /**
     * HeartbeatManager 인스턴스를 받아온다. HeartbeatManager는 Cluster마다 Singleton 형태로 작동하며 반드시 이 메서드를 통해 인스턴스를
     * 받아야 한다.
     *
     * @return HeartbeatManager 인스턴스
     */
    public static HeartbeatManager getInstance() {
        return HeartbeatManagerHolder.INSTANCE;
    }

    /**
     * 현재 HeartbeatManager가 연결하고 있는 ZookeeperClient 객체를 받아온다.
     *
     * @return ZookeeperClient 객체
     */
    public ZookeeperClient getZookeeperClient() {
        if (zkClient == null)
            throw new IllegalStateException("Zookeeper Heartbeat가 정지 상태 입니다.");

        return zkClient;
    }

    /**
     * Zookeeper와 연결을 시도하고, Heartbeat 유지를 시작한다.
     *
     * @param uri               Zookeeper 서버 주소
     * @param timeout           Zookeeper 서버 타임아웃 시간
     * @param jassConfiguration 보안을 위한 JAAS 설정 파일 위치
     * @param uuid              현재 클러스터의 UUID
     * @param type              현재 클러스터의 종류
     */
    public void startHeartbeat(String uri, int timeout, String jassConfiguration, UUID uuid, String type) {
        if (zkClient != null)
            throw new IllegalStateException("Zookeeper를 통한 Heartbeat가 이미 작동중 입니다.");

        try {
            zkClient = new ZookeeperClient(uri, timeout, jassConfiguration);
        } catch (ZookeeperException e) {
            throw new RuntimeException("Zookeeper 연결에 실패했습니다. " + e.getMessage());
        }

        this.uuid = uuid;
        this.type = type;

        createHeartbeatFile();
    }

    /**
     * Heartbeat 유지를 멈추고, Zookeeper 서버와의 연결을 종료한다.
     */
    public void stopHeartbeat() {
        deleteHeartbeatFile();

        ZookeeperClient zkClient = getZookeeperClient();

        try {
            zkClient.close();
        } catch (ZookeeperException e) {
            e.printStackTrace();
        } finally {
            this.zkClient = null;
        }
    }

    /**
     * 주어진 UUID를 갖는 Cluster의 예상 Heartbeat File 경로를 돌려줍니다.
     *
     * @param uuid Cluster의 UUID.
     * @return 예상 Heartbeat File의 경로.
     */
    public String getHeartbeatPath(UUID uuid) {
        return HEARTBEAT_PATH + uuid.toString();
    }

    private void createHeartbeatFile() {
        ZookeeperClient zkClient = getZookeeperClient();

        String heartbeatPath = getHeartbeatPath(uuid);

        try {
            deleteHeartbeatFile();

            zkClient.createFile(heartbeatPath, type, false);
        } catch (ZookeeperException e) {
            throw new RuntimeException("Heartbeat File 생성에 실패했습니다. " + e.getMessage());
        }
    }

    private void deleteHeartbeatFile() {
        ZookeeperClient zkClient = getZookeeperClient();

        String heartbeatPath = getHeartbeatPath(uuid);

        try {
            if (zkClient.exists(heartbeatPath))
                zkClient.delete(heartbeatPath);
        } catch (ZookeeperException e) {
            throw new RuntimeException("Heartbeat File 제거에 실패했습니다. " + e.getMessage());
        }
    }

    /**
     * 주어진 UUID의 Cluster가 현재 Cluster List의 등록이 된 바른 Cluster인지 확인한다.
     *
     * @param uuid 확인할 Cluster의 UUID.
     * @return 올바른 Cluster이면 true, 아니라면 false를 돌려준다.
     */
    public boolean isValidCluster(UUID uuid) {
        ZookeeperClient zkClient = getZookeeperClient();

        String heartbeatPath = getHeartbeatPath(uuid);

        try {
            if (zkClient.exists(heartbeatPath))
                return true;
        } catch (ZookeeperException e) {
            return false;
        }

        return false;
    }

    /**
     * 주어진 UUID의 Cluster가 어떤 Type의 Cluster인지 확인한다.
     *
     * @param uuid 확인할 Cluster의 UUID.
     * @return 해당 Cluster의 Type.
     */
    public String getClusterType(UUID uuid) {
        if (!isValidCluster(uuid))
            throw new IllegalArgumentException("올바른 Cluster UUID가 아닙니다.");

        ZookeeperClient zkClient = getZookeeperClient();
        String heartbeatPath = getHeartbeatPath(uuid);

        try {
            return zkClient.get(heartbeatPath);
        } catch (ZookeeperException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
