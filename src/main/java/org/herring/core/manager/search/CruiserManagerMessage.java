package org.herring.core.manager.search;

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

    public CruiserManagerMessage(UUID uuid, List<Map<String, String>> data) {
        this.uuid = uuid;
        this.data = data;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

}
