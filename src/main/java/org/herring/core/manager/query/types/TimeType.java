package org.herring.core.manager.query.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 시간을 대표하는 데이터 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class TimeType extends BaseType<Calendar> implements Comparable<TimeType> {

    private static final long serialVersionUID = 1389684777539441981L;
    private static final List<DateFormat> patternList = new ArrayList<DateFormat>() {
        {
            add(new SimpleDateFormat("yyyy-MM-dd"));
            add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
            add(new SimpleDateFormat("yyyy-MM-dd'Z'Z"));
            add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z"));
        }
    };

    public TimeType(Calendar value) {
        super(Calendar.class, value);
    }

    public TimeType(TimeType value) {
        this((Calendar) value.getValue().clone());
    }

    @Override
    protected Calendar copyValueInstance(Calendar value) {
        return (Calendar) value.clone();
    }

    @Override
    public int compareTo(TimeType o) {
        Calendar a = getValue();
        Calendar b = o.getValue();

        return a.compareTo(b);
    }

    public static TimeType valueOf(String value) {
        boolean flag = false;
        Calendar calendar = Calendar.getInstance();

        for (DateFormat format : patternList) {
            try {
                Date date = format.parse(value);

                calendar.setTime(date);
                flag = true;
                break;
            } catch (ParseException ignored) {}
        }

        if (!flag)
            throw new IllegalArgumentException("문자열 형식이 올바르지 않습니다.");

        return new TimeType(calendar);
    }

    public static TimeType valueOf(String date, String time, String zone) {
        return valueOf(date + "T" + time + "Z" + zone);
    }

    public long getTimeInMillis() {
        return getValue().getTimeInMillis();
    }

    @Override
    public String toString() {
        return "[<TimeType> value=" + getValue().toString() + "]";
    }
}
