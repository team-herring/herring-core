package org.herring.core.manager.query;

import org.herring.core.manager.query.types.DateTimeType;
import org.herring.core.manager.query.types.FieldType;

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
    private DateTimeType start;
    private DateTimeType end;

    public LoadCommand(FieldType dataSet, DateTimeType start, DateTimeType end) {
        super(CommandType.LOAD);

        setDataSet(dataSet);
        setDateRange(start, end);
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
    public DateTimeType getStartTime() {
        return start;
    }

    /**
     * 정해진 시간 범위의 끝 시간을 가져온다.
     *
     * @return 지정된 시간 범위의 끝
     */
    public DateTimeType getEndTime() {
        return end;
    }

    private void setDateRange(DateTimeType start, DateTimeType end) {
        if (start.compareTo(end) > 0)
            throw new IllegalArgumentException("시작 시간은 반드시 끝 시간 보다 빨라야 합니다.");

        this.start = start;
        this.end = end;
    }
}
