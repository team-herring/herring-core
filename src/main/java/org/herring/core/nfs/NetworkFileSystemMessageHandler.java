package org.herring.core.nfs;

import org.herring.core.protocol.NetworkContext;
import org.herring.core.protocol.handler.SyncMessageHandler;

/**
 * << Description >>
 * User: hyunje
 * Date: 13. 6. 10.
 * Time: 오후 1:06
 */
public class NetworkFileSystemMessageHandler extends SyncMessageHandler {
    @Override
    public boolean messageArrived(NetworkContext context, Object data) throws Exception {
        return super.messageArrived(context, data);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
