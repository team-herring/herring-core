package org.herring.core.cluster;

import org.apache.zookeeper.KeeperException;
import org.herring.core.cluster.zookeeper.ZookeeperClient;
import org.herring.core.cluster.zookeeper.ZookeeperWatchListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 다수의 클러스터가 공통으로 저장하고 정보를 공유하는 목적으로 설계된 클래스다. (또는 저장할 정보가 작은 경우에도 활용할 수 있다.)
 * 모든 클러스터에서 생성된 ClusterSharedStorage 객체는 반드시 서로 동등한 자료를 보유하고 있음을 보장한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ClusterSharedStorage {

    private UUID uuid;
    private ZookeeperClient zkClient;

    private static final String STORAGE_PATH = "/herring/storage";

    private Map<String, String> storageMap;
    private final Object lockObject;
    private final StorageUpdateListener UPDATE_LISTENER;

    public ClusterSharedStorage(ZookeeperClient zkClient) {
        this(zkClient, UUID.randomUUID());
    }

    public ClusterSharedStorage(ZookeeperClient zkClient, UUID uuid) {
        this.zkClient = zkClient;
        this.uuid = uuid;

        lockObject = new Object();
        storageMap = new HashMap<String, String>();

        UPDATE_LISTENER = new StorageUpdateListener();

        initializeStorage();
    }

    public String get(String key) {
        if (key.contains("/"))
            throw new IllegalArgumentException("'/' character cannot be in key.");

        synchronized (lockObject) {
            return storageMap.get(key);
        }
    }

    public void put(String key, String value) {
        ZookeeperClient zkClient = getZookeeperClient();

        if (key.contains("/"))
            throw new IllegalArgumentException("'/' character cannot be in key.");

        String path = getPath(key);

        synchronized (lockObject) {
            try {
                if (zkClient.exists(path))
                    zkClient.update(path, value);
                else {
                    zkClient.createFile(path, value, false);
                    zkClient.addEventListener(path, UPDATE_LISTENER);
                }

                storageMap.put(key, value);
            } catch (KeeperException e) {
                throw new RuntimeException("Cannot put data into Zookeeper Storage. " + e.getMessage());
            }
        }
    }

    public boolean containsKey(String key) {
        synchronized (lockObject) {
            return storageMap.containsKey(key);
        }
    }

    public void remove(String key) {
        ZookeeperClient zkClient = getZookeeperClient();
        String path = getPath(key);

        synchronized (lockObject) {
            try {
                if (zkClient.exists(path))
                    zkClient.delete(path);

                storageMap.remove(key);
            } catch (KeeperException e) {
                throw new RuntimeException("Cannot remove data in Zookeeper Storage. " + e.getMessage());
            }
        }
    }

    private String getPath(String key) {
        if (key != null)
            return STORAGE_PATH + "/" + uuid.toString() + "/" + key;
        return STORAGE_PATH + "/" + uuid.toString();
    }

    private String getKey(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void initializeStorage() {
        ZookeeperClient zkClient = getZookeeperClient();
        String path = getPath(null);

        try {
            // whole storage directory.
            if (!zkClient.exists(STORAGE_PATH))
                zkClient.createDirectory(STORAGE_PATH);

            // private storage directory
            if (!zkClient.exists(path))
                zkClient.createDirectory(path);

            List<String> children = zkClient.getChildren(path);
            for (String child : children) {
                String key = child;
                String value = zkClient.get(path + "/" + child);

                storageMap.put(key, value);
            }
        } catch (KeeperException e) {
            throw new RuntimeException("Cannot initialize Zookeeper Storage. " + e.getMessage());
        }
    }

    private class StorageUpdateListener extends ZookeeperWatchListener {
        @Override
        public boolean nodeDataChanged(String path) {
            String key = getKey(path);

            synchronized (lockObject) {
                try {
                    String value = zkClient.get(path);

                    storageMap.put(key, value);
                } catch (KeeperException ignored) {}
            }

            return true;
        }
    }

    private ZookeeperClient getZookeeperClient() {
        if (!zkClient.isConnected())
            throw new IllegalStateException("Zookeeper Server is not connected.");

        return zkClient;
    }

}
