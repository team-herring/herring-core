package org.herring.core.manager.query;

/**
 * 현재 데이터 셋 데크 최상위에 위치한 데이터 셋을 Manager에게 돌려주는 명령을 나타내는 클래스다.
 * 이 명령은 명령 리스트의 가장 마지막에 오는 것이 보장된다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class ReturnCommand extends QueryCommand {

    private static final long serialVersionUID = 4955292458133269277L;

    public ReturnCommand() {
        super(CommandType.RETURN);
    }

    @Override
    public String toString() {
        return "<Return>";
    }
}
