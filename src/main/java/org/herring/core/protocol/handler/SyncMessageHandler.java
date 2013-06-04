package org.herring.core.protocol.handler;

import org.herring.core.protocol.NetworkContext;

/**
 * 동기 통신을 위해 사용하는 기본 핸들러 클래스.
 * 동기 통신을 한다면 이 메서드를 사용하는 것을 추천하나, 비동기와 동기를 섞어서 통신할 경우 이 클래스 또는 @{code AsyncMessageHandler}를 오버라이드 하여 적절하게 사용해야한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class SyncMessageHandler extends MessageHandler {
    @Override
    public boolean messageArrived(NetworkContext context, Object data) throws Exception {
        return false;
    }
}
