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

    @Test
    public void testCodecTest() throws Exception {
        HerringCodec codec = new CruiserManagerMessageCodec();

        UUID uuid = UUID.randomUUID();
        List<Map<String, String>> rows = new LinkedList<Map<String, String>>();

        Map<String, String> data1 = new HashMap<String, String>();
        data1.put("@id", "1234");
        data1.put("@col1", "hello");
        data1.put("@col2", "bye");

        Map<String, String> data2 = new HashMap<String, String>();
        data2.put("@id", "1235");
        data2.put("@col1", "tired");
        data2.put("@col2", "hot6");

        rows.add(data1);
        rows.add(data2);

        CruiserManagerMessage message = new CruiserManagerMessage(uuid, rows);
        byte[] encoded = codec.encode(message);

        System.out.println(encoded.length);

        CruiserManagerMessage decoded = (CruiserManagerMessage) codec.decode(encoded);

        Assert.assertEquals(message.getData(), decoded.getData());
        Assert.assertEquals(message.getUUID(), decoded.getUUID());
    }

}
