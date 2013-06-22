package org.herring.core.protocol.codec;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: hyunje Date: 13. 5. 28. Time: 오전 8:53
 */
public class CruiserAgentConnectionCodec implements HerringCodec {
    @Override
    public byte[] encode(Object o) throws Exception {
        if (o == null) throw new Exception();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o).getBytes("utf-8");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object decode(byte[] bytes) throws Exception {
        List<Map<String, String>> dataList = null;
        ObjectMapper mapper = new ObjectMapper();
        String data = new String(bytes, "utf-8");

        dataList = mapper.readValue(data, ArrayList.class);

        return dataList;
    }
}
