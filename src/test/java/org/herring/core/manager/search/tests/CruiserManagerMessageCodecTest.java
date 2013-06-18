package org.herring.core.manager.search.tests;

import org.herring.core.manager.search.CruiserManagerMessage;
import org.herring.core.manager.search.CruiserManagerMessageCodec;
import org.herring.core.protocol.codec.HerringCodec;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

/**
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CruiserManagerMessageCodecTest {

    private Map<String, String> getSampleData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("@id", "1234");
        data.put("@col1", "hello");
        data.put("@col2", "bye");

        return data;
    }

    private void AssertEquals(CruiserManagerMessage expected, CruiserManagerMessage actual) {
        Assert.assertEquals(expected.getData(), actual.getData());
        Assert.assertEquals(expected.getType(), actual.getType());
        Assert.assertEquals(expected.getUUID(), actual.getUUID());
    }

    @Test
    public void testCodecTest() throws Exception {
        HerringCodec codec = new CruiserManagerMessageCodec();

        UUID uuid = UUID.randomUUID();
        List<Map<String, String>> rows = new LinkedList<Map<String, String>>();

        rows.add(getSampleData());
        rows.add(getSampleData());

        CruiserManagerMessage message = new CruiserManagerMessage(uuid, CruiserManagerMessage.Type.SEARCH_RESULT, rows);
        byte[] encoded = codec.encode(message);

        System.out.println(encoded.length);

        CruiserManagerMessage decoded = (CruiserManagerMessage) codec.decode(encoded);

        AssertEquals(message, decoded);
    }

    @Test
    public void testCodecWithUUIDNullData() throws Exception {
        HerringCodec codec = new CruiserManagerMessageCodec();

        List<Map<String, String>> rows = new LinkedList<Map<String, String>>();

        rows.add(getSampleData());

        CruiserManagerMessage message = new CruiserManagerMessage(null, CruiserManagerMessage.Type.SEARCH_RESULT, rows);
        byte[] encoded = codec.encode(message);

        CruiserManagerMessage decoded = (CruiserManagerMessage) codec.decode(encoded);

        AssertEquals(message, decoded);
    }

    @Test
    public void testCodecWithNullData() throws Exception {
        HerringCodec codec = new CruiserManagerMessageCodec();

        CruiserManagerMessage message = new CruiserManagerMessage(null, null, null);
        byte[] encoded = codec.encode(message);

        CruiserManagerMessage decoded = (CruiserManagerMessage) codec.decode(encoded);

        AssertEquals(message, decoded);
    }

}
