package org.herring.core.protocol.handler;

import org.herring.core.protocol.NetworkContext;

/**
 * @author Chiwan Park
 * @since 0.1
 */
public class SyncMessageHandler extends MessageHandler {
    @Override
    public boolean messageArrived(NetworkContext context, Object data) throws Exception {
        return false;
    }
}
