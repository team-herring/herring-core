package org.herring.core.cluster.tests;

import org.herring.core.cluster.HeartbeatManager;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Properties;
import java.util.UUID;

/**
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HeartbeatManagerTest {

    private static String uri;
    private static int timeout;
    private static UUID uuid;
    private static String jassConfiguration;

    @BeforeClass
    public static void setup() throws Exception {
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("zookeeper.properties"));

        uri = properties.getProperty("zookeeper.server-uri");
        timeout = Integer.valueOf(properties.getProperty("zookeeper.timeout"));
        uuid = UUID.randomUUID();
        jassConfiguration = ClassLoader.getSystemResource("jass.conf").toString();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        HeartbeatManager heartbeatManager = HeartbeatManager.getInstance();

        heartbeatManager.stopHeartbeat();
    }

    @Test
    public void test0startHeartbeat() throws Exception {
        HeartbeatManager heartbeatManager = HeartbeatManager.getInstance();
        heartbeatManager.startHeartbeat(uri, timeout, jassConfiguration, uuid, "sample");
    }

    @Test
    public void test1checkHeartbeatFile() throws Exception {
        HeartbeatManager heartbeatManager = HeartbeatManager.getInstance();

        Assert.assertTrue(heartbeatManager.isValidCluster(uuid));
    }

    @Test
    public void test2checkHeartbeatType() throws Exception {
        HeartbeatManager heartbeatManager = HeartbeatManager.getInstance();

        Assert.assertEquals(heartbeatManager.getClusterType(uuid), "sample");
    }
}
