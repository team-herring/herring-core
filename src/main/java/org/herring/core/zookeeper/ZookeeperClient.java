package org.herring.core.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.client.ZooKeeperSaslClient;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * 실제 Zookeeper 서버와 통신하는 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ZookeeperClient {

    private ZooKeeper zk = null;

    /**
     * Zookeeper 서버에 연결하고, 해당 연결을 지속한다.
     *
     * @param uri     Zookeeper 서버의 주소 (ex: localhost:2181)
     * @param timeout Zookeeper 서버에 명령 시도시 timeout 시간 (milliseconds 단위)
     * @throws IOException 네트워크에 의한 이유로 연결에 실패하였을 경우
     */
    public ZookeeperClient(String uri, int timeout, String jassConfiguration) throws IOException {
        // TODO: Zookeeper SASL을 위한 설정. 추후 세련된 방법으로 설정하도록 수정할 것.
        System.setProperty("zookeeper.authProvider.1", "org.apache.zookeeper.server.auth.SASLAuthenticationProvider");
        System.setProperty("java.security.auth.login.config", jassConfiguration);
        System.setProperty(ZooKeeperSaslClient.LOGIN_CONTEXT_NAME_KEY, "Client");

        zk = new ZooKeeper(uri, timeout, null);
    }

    /**
     * Zookeeper 서버에 파일을 생성하고, 데이터를 입력한다.
     *
     * @param path         파일을 생성할 경로
     * @param data         파일에 포함시킬 데이터
     * @param isPersistent 만약 true라면 세션의 연결 여부와 상관없이 데이터가 지속되며, false일 경우 EPHEMERAL 모드로 파일을 생성하여, 세션 종료시 파일이 삭제되도록 한다.
     */
    public void createFile(String path, String data, boolean isPersistent) throws KeeperException {
        try {
            if (zk.exists(path, false) == null) {
                byte[] dataBytes = data != null ? data.getBytes() : null;
                zk.create(path, dataBytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, isPersistent ? CreateMode.PERSISTENT : CreateMode.EPHEMERAL);
            } else {
                throw new IllegalArgumentException(path + "는 이미 존재하는 경로 입니다.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Zookeeper 서버에 디렉토리를 생성한다.
     *
     * @param path 디렉토리를 생성할 경로
     */
    public void createDirectory(String path) throws KeeperException {
        createFile(path, null, true);
    }

    /**
     * Zookeeper 서버에 저장된 데이터를 새로이 갱신한다.
     *
     * @param path 갱신할 파일의 경로
     * @param data 새로 입력할 데이터
     */
    public void update(String path, String data) throws KeeperException {
        try {
            if (zk.exists(path, false) == null) {
                throw new IllegalArgumentException(path + "는 존재하지 않는 경로 입니다.");
            } else {
                byte[] dataBytes = data != null ? data.getBytes() : null;
                zk.setData(path, dataBytes, -1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Zookeeper 서버로 부터 지정된 경로에 저장된 데이터를 읽어온다.
     *
     * @param path 데이터를 가져올 파일의 경로
     * @return 파일의 데이터를 String 형태로 돌려준다.
     */
    public String get(String path) throws KeeperException {
        try {
            Stat stat = new Stat();
            byte[] dataBytes = zk.getData(path, false, stat);

            return new String(dataBytes);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "";
    }

    /**
     * Zookeeper 서버에서 지정된 경로의 데이터를 삭제한다.
     *
     * @param path 삭제할 데이터의 경로
     */
    public void delete(String path) throws KeeperException {
        try {
            zk.delete(path, -1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 주어진 경로에 위치한 파일 또는 디렉토리의 Stat 정보를 돌려준다.
     *
     * @param path Stat 정보를 얻을 경로
     * @return 주어진 경로에 위치한 파일 또는 디렉토리의 Stat 정보
     */
    private Stat getStat(String path) throws KeeperException {
        try {
            return zk.exists(path, false);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    /**
     * 주어진 경로에 파일 또는 디렉토리가 존재하는지 확인한다.
     *
     * @param path 존재 여부를 확인할 경로
     * @return 주어진 경로에 파일 또는 디렉토리가 존재한다면 true, 그렇지 않을 경우 false를 돌려준다.
     */
    public boolean exists(String path) throws KeeperException {
        return exists(path, null);
    }

    /**
     * 주어진 경로에 파일 또는 디렉토리가 존재하는지 확인한다.
     *
     * @param path    존재 여부를 확인할 경로
     * @param watcher 추후 이벤트를 통지 받을 내부 리스너 객체
     * @return 주어진 경로에 파일 또는 디렉토리가 존재한다면 true, 그렇지 않을 경우 false를 돌려준다.
     */
    public boolean exists(String path, final Watcher watcher) throws KeeperException {
        try {
            return zk.exists(path, watcher) != null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return false;
    }

    /**
     * 지정 경로의 자식 노드의 갯수를 돌려준다.
     *
     * @param path 자식 노드의 갯수를 알아볼 디렉토리 경로
     * @return 주어진 디렉토리 경로의 자식 노드의 수
     */
    public int getNumChildren(String path) throws KeeperException {
        return this.getStat(path).getNumChildren();
    }

    /**
     * 지정 경로에 위치한 디렉토리 또는 파일의 생성 시간을 돌려준다.
     *
     * @param path 생성 시간을 알아볼 파일 또는 디렉토리의 경로
     * @return 주어진 경로에 위치한 파일 또는 디렉토리의 생성 시간
     */
    public long getCreatedTime(String path) throws KeeperException {
        return this.getStat(path).getCtime();
    }

    /**
     * 지정 경로에 위치한 디렉토리 또는 파일의 최근 수정 시간을 돌려준다.
     *
     * @param path 최근 수정 시간을 알아볼 파일 또는 디렉토리의 경로
     * @return 주어진 경로에 위치한 파일 또는 디렉토리의 최근 수정 시간
     */
    public long getModifiedTime(String path) throws KeeperException {
        return this.getStat(path).getMtime();
    }

    /**
     * 지정 경로에 위치한 자식 노드 리스트를 반환한다.
     *
     * @param path 자식 노드 리스트를 확인할 경로
     * @return 주어진 경로 아래 존재하는 노드들의 path를 돌려준다.
     */
    public List<String> getChildren(String path) throws KeeperException {
        return getChildren(path, null);
    }

    /**
     * 지정 경로에 위치한 자식 노드 리스트를 반환한다.
     *
     * @param path    자식 노드 리스트를 확인할 경로
     * @param watcher 추후 이벤트를 통지 받을 내부 리스너 객체
     * @return 주어진 경로 아래 존재하는 노드들의 path를 돌려준다.
     */
    public List<String> getChildren(String path, final Watcher watcher) throws KeeperException {
        try {
            return zk.getChildren(path, watcher);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    /**
     * 명시적으로 Zookeeper 서버와의 연결을 종료한다. 만약 이미 연결이 종료된 상태라면 아무일도 하지 않는다.
     */
    public void close() {
        if (zk == null)
            return;

        try {
            zk.close();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        zk = null;
    }

    /**
     * GC에 의해 객체 해제시 서버와의 연결을 종료한다.
     *
     * @deprecated 이 메서드는 GC에 의핸 해제를 위해 존재하며 대부분의 경우 호출할 필요가 없다. 대신 close() 메서드를 사용해야한다.
     */
    @Deprecated
    public void finalize() throws Throwable {
        super.finalize();

        close();
    }

    /**
     * 지정된 노드에 감시를 위한 Event Listener를 등록한다. 지정할 노드는 반드시 존재해야한다.
     *
     * @param path     감시를 할 노드의 경로
     * @param listener 감시 결과를 통보 받을 Event Listener
     */
    public void addEventListener(final String path, final ZookeeperWatchListener listener) {
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                String path = event.getPath();
                boolean flag = false;
                Event.EventType type = event.getType();

                switch (type) {
                    case NodeDeleted:
                        listener.nodeDeleted(path);
                        break;
                    case NodeDataChanged:
                        flag = listener.nodeDataChanged(path);
                        break;
                    case NodeChildrenChanged:
                        flag = listener.childrenNodeChanged(path);
                        break;
                }

                if (flag) {
                    try {
                        exists(path, this);
                        getChildren(path, this);
                    } catch (KeeperException ignored) {}
                }
            }
        };

        try {
            if (!exists(path))
                // TODO: Exception 처리
                return;

            exists(path, watcher);
            getChildren(path, watcher);
        } catch (KeeperException ignored) {}
    }
}
