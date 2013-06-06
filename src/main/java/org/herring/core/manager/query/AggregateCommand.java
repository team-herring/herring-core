package org.herring.core.manager.query;

import org.herring.core.manager.query.types.FieldType;

/**
 * 이 클래스는 현재 데크 최상위의 데이터 셋에 대해 집계 연산을 수행하는 명령을 나타낸다.
 * 집계 연산이 수행 되고 나서, 집계 연산을 수행했던 데이터 셋은 데크에서 제거되고, 데크 최상단에는 집계 결과가 담긴 데이터 셋이 존재해야한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public abstract class AggregateCommand extends QueryCommand {

    private static final long serialVersionUID = -4148499401919382882L;

    /**
     * 집계 연산의 종류를 나타내는 상수다.
     * <p/>
     * AVERAGE: 그루핑된 집합의 평균을 계산한다.
     * SUM: 그루핑된 집합의 총 합을 계산한다.
     * MIN: 그루핑된 집합의 최솟값을 구한다.
     * MAX: 그루핑된 집합의 최댓값을 구한다.
     * MEDIAN: 그루핑된 집합의 중앙값을 구한다.
     * COUNT: 그루핑된 집합의 지정 필드의 유효 값의 갯수를 구한다.
     */
    public enum AggregateFunction {
        AVERAGE, SUM, MIN, MAX, MEDIAN, COUNT
    }

    /**
     * 집계 연산의 그루핑 기준을 나타내는 상수다.
     * <p/>
     * FIELD: 그루핑 기준이 특정 필드임을 나타낸다. 객체를 @{link AggregateByFieldCommand}로 캐스팅하여 사용한다.
     * TIME: 그루핑 기준이 시간 간격임을 나타낸다. 객체를 @{link AggregateByTimeCommand}로 캐스팅하여 사용한다.
     */
    public enum AggregateMethod {
        FIELD, TIME
    }

    private AggregateFunction aggregateFunction;
    private AggregateMethod aggregateMethod;
    private FieldType aggregatedField;
    private FieldType resultField;

    public AggregateCommand(AggregateFunction aggregateFunction, AggregateMethod aggregateMethod, FieldType aggregatedField, FieldType resultField) {
        super(CommandType.AGGREGATE);

        setAggregateFunction(aggregateFunction);
        setAggregateMethod(aggregateMethod);
        setAggregatedField(aggregatedField);
        setResultField(resultField);
    }

    /**
     * 집계 연산의 종류를 받아온다.
     *
     * @return 집계 연산의 종류를 나타내는 상수
     */
    public AggregateFunction getAggregateFunction() {
        return aggregateFunction;
    }

    private void setAggregateFunction(AggregateFunction aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    /**
     * 그루핑 기준을 받아온다.
     *
     * @return 그루핑 기준을 나타내는 상수
     */
    public AggregateMethod getAggregateMethod() {
        return aggregateMethod;
    }

    private void setAggregateMethod(AggregateMethod aggregateMethod) {
        this.aggregateMethod = aggregateMethod;
    }

    /**
     * 집계 연산을 수행할 필드를 받아온다.
     *
     * @return 집계 연산을 수행할 필드 정보
     */
    public FieldType getAggregatedField() {
        return aggregatedField;
    }

    private void setAggregatedField(FieldType aggregatedField) {
        this.aggregatedField = aggregatedField;
    }

    /**
     * 집계 결과를 저장할 새로운 필드를 받아온다.
     *
     * @return 집계 연산을 저장할 필드 정보
     */
    public FieldType getResultField() {
        return resultField;
    }

    private void setResultField(FieldType resultField) {
        this.resultField = resultField;
    }

    @Override
    public String toString() {
        return "<Aggregate> function=" + getAggregateFunction().toString() + ", aggregateMethod=" + getAggregateMethod().toString();
    }
}
