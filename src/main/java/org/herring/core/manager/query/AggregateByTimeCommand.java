package org.herring.core.manager.query;

import org.herring.core.manager.query.types.FieldType;
import org.herring.core.manager.query.types.NumberType;

/**
 * 이 클래스는 @{link AggregateCommand}의 하위 클래스로써, 집계 연산의 기준이 시간임을 나타낸다.
 * 데이터를 그룹화하는데 기준이 되는 시간은 초 단위로 주어진다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class AggregateByTimeCommand extends AggregateCommand {

    private static final long serialVersionUID = 8356304178039704108L;

    private NumberType groupingTimeSpan;

    public AggregateByTimeCommand(AggregateMethod aggregateMethod, GroupMethod groupMethod, FieldType aggregatedField, FieldType resultField, NumberType groupingTimeSpan) {
        super(aggregateMethod, groupMethod, aggregatedField, resultField);

        setGroupingTimeSpan(groupingTimeSpan);
    }

    public NumberType getGroupingTimeSpan() {
        return groupingTimeSpan;
    }

    private void setGroupingTimeSpan(NumberType groupingTimeSpan) {
        this.groupingTimeSpan = groupingTimeSpan;
    }
}
