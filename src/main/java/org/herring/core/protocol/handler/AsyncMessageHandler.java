package org.herring.core.protocol.handler;

import org.herring.core.protocol.NetworkContext;

/**
 * 현재 통신이 비동기 통신임을 표시하기 위한 태깅 클래스.
 * 실제로 사용하려면 반드시 @{code messageArrived} 메서드를 오버라이드하여 사용하여야 한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class AsyncMessageHandler extends MessageHandler {
    @Override
    public boolean messageArrived(NetworkContext context, Object data) throws Exception {
        return true;
    }
}
