package org.herring.core.manager.query.types;

import java.util.AbstractMap;

/**
 * 시간 구간을 나타내는 데이터 클래스다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class TimeRangeType extends BaseType<AbstractMap.SimpleEntry<DateTimeType, DateTimeType>> {

    private static final long serialVersionUID = 5004928681243791888L;

    public TimeRangeType(AbstractMap.SimpleEntry<DateTimeType, DateTimeType> value) {
        super(AbstractMap.SimpleEntry.class, value);

        if (value.getKey().compareTo(value.getValue()) > 0)
            throw new IllegalArgumentException("시작 시간은 반드시 끝 시간보다 작거나 같아야 합니다.");
    }

    public TimeRangeType(DateTimeType start, DateTimeType end) {
        this(new AbstractMap.SimpleEntry<DateTimeType, DateTimeType>(new DateTimeType(start), new DateTimeType(end)));
    }

    @Override
    protected AbstractMap.SimpleEntry<DateTimeType, DateTimeType> copyValueInstance(AbstractMap.SimpleEntry<DateTimeType, DateTimeType> value) {
        DateTimeType timeStart = new DateTimeType(value.getKey().getValue());
        DateTimeType timeEnd = new DateTimeType(value.getValue().getValue());

        return new AbstractMap.SimpleEntry<DateTimeType, DateTimeType>(timeStart, timeEnd);
    }

    @Override
    public String toString() {
        DateTimeType timeStart = getValue().getKey();
        DateTimeType timeEnd = getValue().getValue();

        return "[<TimeRangeType> start=" + timeStart.toString() + ", end=" + timeEnd.toString() + "]";
    }

    public DateTimeType getStartTime() {
        return getValue().getKey();
    }

    public DateTimeType getEndTime() {
        return getValue().getValue();
    }
}
