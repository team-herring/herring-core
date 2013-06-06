package org.herring.core.manager.query;

import org.herring.core.manager.query.types.FieldType;

/**
 * 이 클래스는 @{link AggregateCommand의} 하위 클래스로써, 집계 연산의 기준이 특정 필드임을 나타낸다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class AggregateByFieldCommand extends AggregateCommand {

    private static final long serialVersionUID = -7768218216273924106L;

    private FieldType groupingField;

    public AggregateByFieldCommand(AggregateMethod aggregateMethod, GroupMethod groupMethod, FieldType aggregatedField, FieldType resultField, FieldType groupingField) {
        super(aggregateMethod, groupMethod, aggregatedField, resultField);

        setGroupingField(groupingField);
    }

    public FieldType getGroupingField() {
        return groupingField;
    }

    private void setGroupingField(FieldType groupingField) {
        this.groupingField = groupingField;
    }

    @Override
    public String toString() {
        String strSuper = super.toString();

        return strSuper + ", groupingField=" + getGroupingField().toString();
    }
}
