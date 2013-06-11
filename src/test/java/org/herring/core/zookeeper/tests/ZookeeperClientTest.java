package org.herring.core.zookeeper.tests;

import org.herring.core.zookeeper.ZookeeperClient;
import org.herring.core.zookeeper.ZookeeperWatchListener;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ZookeeperClient 클래스 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZookeeperClientTest {
    private static ZookeeperClient zkClient;
    private static Properties properties;
    private static List<Boolean> watchCheck;

    @BeforeClass
    public static void setup() throws Exception {
        properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("zookeeper.properties"));

        String uri = properties.getProperty("zookeeper.server-uri");
        int timeout = Integer.valueOf(properties.getProperty("zookeeper.timeout"));

        watchCheck = new ArrayList<Boolean>();
        for (int i = 0; i < 3; ++i)
            watchCheck.add(false);

        zkClient = new ZookeeperClient(uri, timeout, ClassLoader.getSystemResource("jass.conf").toString());
    }

    @AfterClass
    public static void cleanup() throws Exception {
        zkClient.delete("/herring-zkClient-test/sample");
        zkClient.delete("/herring-zkClient-test");
    }

    @Test
    public void testCreateDirectory() throws Exception {
        zkClient.createDirectory("/herring-zkClient-test");
    }

    @Test
    public void testCreateFile() throws Exception {
        zkClient.createFile("/herring-zkClient-test/sample", "file test", false);
    }

    @Test
    public void testDirectoryChildren() throws Exception {
        List<String> children = zkClient.getChildren("/herring-zkClient-test");

        for (String child : children) {
            Assert.assertEquals("sample", child);
        }
    }

    @Test
    public void testExists() throws Exception {
        Assert.assertEquals(true, zkClient.exists("/herring-zkClient-test"));
    }

    @Test
    public void testGet() throws Exception {
        String data = zkClient.get("/herring-zkClient-test/sample");

        Assert.assertEquals("file test", data);
    }

    @Test
    public void testGetNumChildren() throws Exception {
        int numChildren = zkClient.getNumChildren("/herring-zkClient-test");

        Assert.assertEquals(1, numChildren);
    }

    @Test
    public void testUpdate() throws Exception {
        zkClient.update("/herring-zkClient-test/sample", "updated");

        String data = zkClient.get("/herring-zkClient-test/sample");
        Assert.assertEquals("updated", data);
    }

    @Test
    public void testWatch() throws Exception {
        String watchPath = "/herring-zkClient-test/watch";

        zkClient.createDirectory("/herring-zkClient-test/watch");
        zkClient.addEventListener(watchPath, new ZookeeperWatchListener() {
            // TODO: 테스트 용도 AtomicRef 테스트 종료 후 제거 요망.
            private AtomicInteger level = new AtomicInteger(0);

            @Override
            public boolean childrenNodeChanged(String path) {
                System.out.println("Children Node Changed: " + path + ", atomic: " + level.incrementAndGet());
                watchCheck.set(0, true);
                return true;
            }

            @Override
            public boolean nodeDataChanged(String path) {
                System.out.println("Node Data Changed: " + path + ", atomic: " + level.incrementAndGet());
                watchCheck.set(1, true);
                return true;
            }

            @Override
            public boolean nodeDeleted(String path) {
                System.out.println("Node Deleted: " + path + ", atomic: " + level.incrementAndGet());
                watchCheck.set(2, true);
                return true;
            }
        });
    }

    @Test
    public void testWatchCreateFileChildren() throws Exception {
        zkClient.createFile("/herring-zkClient-test/watch/hello1", "sample", false);

        Thread.sleep(200);
    }

    @Test
    public void testWatchModifyData() throws Exception {
        zkClient.update("/herring-zkClient-test/watch", "hi");

        Thread.sleep(200);
    }

    @Test
    public void testWatchRemoveFileChildren() throws Exception {
        zkClient.delete("/herring-zkClient-test/watch/hello1");

        Thread.sleep(200);
    }

    @Test
    public void testWatchRemoveLast() throws Exception {
        zkClient.delete("/herring-zkClient-test/watch");

        Thread.sleep(200);
    }

    @Test
    public void testWatchVerify() throws Exception {
        for (boolean value : watchCheck) {
            Assert.assertTrue(value);
        }
    }
}
