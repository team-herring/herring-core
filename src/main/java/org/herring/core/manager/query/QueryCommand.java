package org.herring.core.manager.query;

import java.io.Serializable;

/**
 * 사용자가 입력한 쿼리를 Herring Cruiser가 수행가능한 형태로 변환한 명령 클래스.
 * Herring Cruiser의 쿼리 명령 수행 구조는 내부적으로 데이터 셋 데크에서 작동한다고 가정한다. 쿼리의 순서는 먼저 나온 명령이 먼저 수행되는 FIFO 구조를 기본으로 한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public abstract class QueryCommand implements Serializable {

    private static final long serialVersionUID = 7372168114576939887L;

    /**
     * 쿼리 명령의 종류를 나타낸다.
     * <p/>
     * LOAD: Storage에서 데이터 셋을 불러오는 Load 명령이다.
     * FILTER: 데이터를 조건에 맞는 경우만 남기고, 나머지를 폐기하는 Filter 명령이다.
     * MERGE: 서로 다른 데이터 셋을 AND 또는 OR 조건에 맞게 병합하는 Merge 명령이다.
     * AGGREGATE: 데이터 셋을 특정 기준에 따라 묶은 뒤, 집합 연산을 수행하는 Aggregate 명령이다.
     * RETURN: Manager에게 데이터 셋을 돌려주는 Return 명령이다.
     */
    public enum CommandType {
        LOAD, FILTER, MERGE, AGGREGATE, RETURN
    }

    private CommandType type;

    public QueryCommand(CommandType type) {
        setType(type);
    }

    /**
     * 쿼리 명령의 종류를 받아온다. 이 메서드를 호출하여 QueryCommand 객체를 캐스팅할 세부 클래스를 결정할 수 있다.
     *
     * @return 쿼리 명령의 종류
     */
    public CommandType getType() {
        return type;
    }

    private void setType(CommandType type) {
        this.type = type;
    }
}
