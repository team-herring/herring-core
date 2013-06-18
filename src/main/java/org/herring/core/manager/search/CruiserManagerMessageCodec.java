package org.herring.core.manager.search;

import org.herring.core.protocol.codec.HerringCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author Chiwan Park
 * @since 0.1
 */
public class CruiserManagerMessageCodec implements HerringCodec {

    private static final byte UUID_FINISH = (byte) 0xF5;
    private static final byte COLUMN_FINISH = (byte) 0xF6;
    private static final byte DATA_FINISH = (byte) 0xF7;
    private static final byte ROW_FINISH = (byte) 0xF8;

    @Override
    public byte[] encode(Object object) throws Exception {
        CruiserManagerMessage message = (CruiserManagerMessage) object;
        ByteArrayOutputStream output = new ByteArrayOutputStream(8192);

        UUID uuid = message.getUUID();
        List<Map<String, String>> rows = message.getData();

        // output uuid
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        ByteBuffer uuidBuffer = ByteBuffer.allocate(16);
        uuidBuffer.putLong(most);
        uuidBuffer.putLong(least);
        output.write(uuidBuffer.array());
        output.write(UUID_FINISH);

        // output type
        output.write(message.getType().getValue());

        // output rows
        for (Map<String, String> row : rows) {
            for (String column : row.keySet()) {
                String data = row.get(column);

                // output column
                output.write(column.getBytes("utf-8"));
                output.write(COLUMN_FINISH);

                // output data
                output.write(data.getBytes("utf-8"));
                output.write(DATA_FINISH);
            }
            output.write(ROW_FINISH);
        }

        return output.toByteArray();
    }

    @Override
    public Object decode(byte[] packet) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(packet);
        List<Map<String, String>> rows = new LinkedList<Map<String, String>>();

        // uuid detection
        ByteBuffer uuidBuffer = ByteBuffer.wrap(packet, 0, 16);
        long most = uuidBuffer.getLong();
        long least = uuidBuffer.getLong();
        UUID uuid = new UUID(most, least);
        input.skip(17);

        // type detection
        byte b = (byte) input.read();
        CruiserManagerMessage.Type type = CruiserManagerMessage.Type.valueOf(b);

        // result detection
        while (input.available() > 0) {
            Map<String, String> row = new HashMap<String, String>();

            while (true) {
                byte[] columnBundle = readByteBundle(input);
                if (columnBundle.length == 0)
                    break;
                byte[] dataBundle = readByteBundle(input);

                String column = new String(columnBundle, "utf-8");
                String data = new String(dataBundle, "utf-8");

                row.put(column, data);
            }

            rows.add(row);
        }

        return new CruiserManagerMessage(uuid, type, rows);
    }

    private byte[] readByteBundle(ByteArrayInputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream(512);

        while (input.available() > 0) {
            byte b = (byte) input.read();
            if (b == UUID_FINISH || b == COLUMN_FINISH || b == DATA_FINISH || b == ROW_FINISH)
                break;

            output.write(b);
        }

        return output.toByteArray();
    }
}
