package org.herring.core.protocol.codec;

import org.codehaus.jackson.map.ObjectMapper;
import org.herring.core.protocol.codec.HerringCodec;

import java.util.List;
import java.util.Map;

/**
 * User: hyunje
 * Date: 13. 5. 28.
 * Time: 오전 8:53
 */
public class CruiserAgentConnectionCodec implements HerringCodec {
    @Override
    public byte[] encode(Object o) throws Exception {
        if (o == null) throw new Exception();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(o);

    }

    @Override
    @SuppressWarnings("unchecked")
    public Object decode(byte[] bytes) throws Exception {
        List<Map<String, String>> dataList = null;
        ObjectMapper mapper = new ObjectMapper();

        String jsonified = mapper.writeValueAsString(dataList);

        dataList = mapper.readValue(jsonified, List.class);

        return dataList;
    }
}
