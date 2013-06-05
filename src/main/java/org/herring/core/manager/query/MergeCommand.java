package org.herring.core.manager.query;

/**
 * 데이터 셋 데크의 최상위의 두 개의 데이터 셋 데크을 하나로 병합하는 명령을 나타내는 클래스이다.
 * 이 명령을 수행한 직후 병합하는 작업에 사용했던 최상위 두 데이터 셋은 데크에서 제거되고, 새로이 병합된 데이터 셋이 데크 최상위에 위치해야한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class MergeCommand extends QueryCommand {

    private static final long serialVersionUID = -2122931770051966533L;

    /**
     * 두 데이터 셋을 병합할 조건을 나타낸다.
     * <p/>
     * AND: 두 데이터 셋 모두에 포함된 데이터를 선택한다. (교집합)
     * OR: 두 데이터 셋 중 한 곳에라도 포함된 데이터는 전부 선택한다. (합집합)
     */
    public enum Condition {
        AND, OR
    }

    private Condition condition;

    public MergeCommand(Condition condition) {
        super(CommandType.MERGE);

        setCondition(condition);
    }

    /**
     * 데이터 셋을 병합할 조건을 받아온다.
     *
     * @return 데이터 셋을 병합할 조건
     */
    public Condition getCondition() {
        return condition;
    }

    private void setCondition(Condition condition) {
        this.condition = condition;
    }
}
