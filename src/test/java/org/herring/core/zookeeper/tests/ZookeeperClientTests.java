package org.herring.core.zookeeper.tests;

import org.herring.core.zookeeper.ZookeeperClient;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Properties;

/**
 * ZookeeperClient 클래스 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZookeeperClientTests {
    private static ZookeeperClient zkClient;
    private static Properties properties;

    @BeforeClass
    public static void setup() throws Exception {
        properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("zookeeper.properties"));

        String uri = properties.getProperty("zookeeper.server-uri");
        int timeout = Integer.valueOf(properties.getProperty("zookeeper.timeout"));

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
}
