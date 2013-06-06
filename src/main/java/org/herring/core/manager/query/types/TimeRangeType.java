package org.herring.core.manager.query.types;

import java.util.AbstractMap;

/**
 * 시간 구간을 나타내는 데이터 클래스다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class TimeRangeType extends BaseType<AbstractMap.SimpleEntry<TimeType, TimeType>> {

    private static final long serialVersionUID = 5004928681243791888L;

    public TimeRangeType(AbstractMap.SimpleEntry<TimeType, TimeType> value) {
        super(AbstractMap.SimpleEntry.class, value);

        if (value.getKey().compareTo(value.getValue()) > 0)
            throw new IllegalArgumentException("시작 시간은 반드시 끝 시간보다 작거나 같아야 합니다.");
    }

    public TimeRangeType(TimeType start, TimeType end) {
        this(new AbstractMap.SimpleEntry<TimeType, TimeType>(new TimeType(start), new TimeType(end)));
    }

    @Override
    protected AbstractMap.SimpleEntry<TimeType, TimeType> copyValueInstance(AbstractMap.SimpleEntry<TimeType, TimeType> value) {
        TimeType timeStart = new TimeType(value.getKey().getValue());
        TimeType timeEnd = new TimeType(value.getValue().getValue());

        return new AbstractMap.SimpleEntry<TimeType, TimeType>(timeStart, timeEnd);
    }

    @Override
    public String toString() {
        TimeType timeStart = getValue().getKey();
        TimeType timeEnd = getValue().getValue();

        return "[<TimeRangeType> start=" + timeStart.toString() + ", end=" + timeEnd.toString() + "]";
    }

    public TimeType getStartTime() {
        return getValue().getKey();
    }

    public TimeType getEndTime() {
        return getValue().getValue();
    }
}
