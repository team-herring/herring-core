package org.herring.core.manager.query;

import org.herring.core.manager.query.types.TimeType;
import org.herring.core.manager.query.types.FieldType;
import org.herring.core.manager.query.types.TimeRangeType;

import java.util.AbstractMap;

/**
 * 데이터 셋을 불러오는 쿼리 명령 클래스다.
 * 명령이 수행된 직후, 불러와진 데이터 셋은 데크 최상위에 위치한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class LoadCommand extends QueryCommand {

    private static final long serialVersionUID = -5646013501815425729L;

    private FieldType dataSet;
    private TimeRangeType timeRange;

    public LoadCommand(FieldType dataSet, TimeRangeType timeRange) {
        super(CommandType.LOAD);

        setDataSet(dataSet);
        setTimeRange(timeRange);
    }

    /**
     * 지정된 데이터 셋의 이름을 가져온다.
     *
     * @return 데이터 셋의 이름
     */
    public FieldType getDataSet() {
        return dataSet;
    }

    private void setDataSet(FieldType dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * 정해진 시간 범위의 시작 시간을 가져온다.
     *
     * @return 지정된 시간 범위의 시작
     */
    public TimeType getStartTime() {
        return getTimeRange().getKey();
    }

    /**
     * 정해진 시간 범위의 끝 시간을 가져온다.
     *
     * @return 지정된 시간 범위의 끝
     */
    public TimeType getEndTime() {
        return getTimeRange().getValue();
    }

    private AbstractMap.SimpleEntry<TimeType, TimeType> getTimeRange() {
        return timeRange.getValue();
    }

    private void setTimeRange(TimeRangeType timeRange) {
        this.timeRange = timeRange;
    }

    @Override
    public String toString() {
        return "<Load> table=" + getDataSet().toString() + ", start=" + getStartTime().toString() + ", end=" + getEndTime().toString();
    }
}
