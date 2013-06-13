package org.herring.core.cluster.tests;

import org.herring.core.cluster.HeartbeatManager;
import org.herring.core.cluster.zookeeper.ZookeeperClient;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Properties;
import java.util.UUID;

/**
 * HeartbeatManager 클래스 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HeartbeatManagerTest {

    private static HeartbeatManager MANAGER;
    private static ZookeeperClient ZK_CLIENT;
    private static String TYPE = "sample-cluster";
    private static UUID CLUSTER_UUID;

    @BeforeClass
    public static void setup() throws Exception {
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("zookeeper.properties"));

        String uri = properties.getProperty("zookeeper.server-uri");
        int timeout = Integer.valueOf(properties.getProperty("zookeeper.timeout"));
        CLUSTER_UUID = UUID.randomUUID();
        String jassConfiguration = ClassLoader.getSystemResource("jass.conf").toString();

        ZK_CLIENT = new ZookeeperClient(uri, timeout, jassConfiguration);
        MANAGER = new HeartbeatManager(ZK_CLIENT, CLUSTER_UUID, TYPE);
    }

    @AfterClass
    public static void cleanup() throws Exception {
        MANAGER.stopHeartbeat();
    }

    @Test
    public void test0startHeartbeat() throws Exception {
        MANAGER.startHeartbeat();
    }

    @Test
    public void test1checkHeartbeatFile() throws Exception {
        Assert.assertTrue(MANAGER.isValidCluster(CLUSTER_UUID));
    }

    @Test
    public void test2checkHeartbeatType() throws Exception {
        Assert.assertEquals(MANAGER.getClusterType(CLUSTER_UUID), TYPE);
    }
}
