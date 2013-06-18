package org.herring.core.manager.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Chiwan Park
 * @since 0.1
 */
public class CruiserManagerMessage {

    private UUID uuid;
    private List<Map<String, String>> data;
    private Type type;

    public enum Type {
        SEARCH_END((byte) 1), SEARCH_RESULT((byte) 2), ERROR((byte) 3), SEARCH_START((byte) 4);

        private byte value;

        private Type(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

        public static Type valueOf(byte b) {
            switch (b) {
                case 1:
                    return SEARCH_END;
                case 2:
                    return SEARCH_RESULT;
                case 3:
                    return ERROR;
                case 4:
                    return SEARCH_START;
            }

            return null;
        }
    }

    public CruiserManagerMessage(UUID uuid, Type type, List<Map<String, String>> data) {
        if (uuid == null)
            uuid = UUID.nameUUIDFromBytes(new byte[16]);
        this.uuid = uuid;

        if (type == null)
            type = Type.SEARCH_START;
        this.type = type;

        if (data == null)
            data = new ArrayList<Map<String, String>>();
        this.data = data;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Type getType() {
        return type;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

}
