package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.TimeType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * TimeType 클래스의 기본 기능 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimeTypeTest {

    @Test
    public void valueOfTest() throws Exception {
        List<TimeType> dateTimeList = new ArrayList<TimeType>();

        dateTimeList.add(TimeType.valueOf("2013-06-03"));
        dateTimeList.add(TimeType.valueOf("2013-06-03T00:00:00"));
        dateTimeList.add(TimeType.valueOf("2013-06-03T09:00:00Z+0900"));

        for (TimeType i : dateTimeList) {
            for (TimeType j : dateTimeList) {
                Assert.assertEquals(i, j);
            }
        }
    }

    @Test
    public void valueOfTest2() throws Exception {
        TimeType date1 = TimeType.valueOf("2013-06-03T13:35:33");
        TimeType date2 = TimeType.valueOf("2013-06-03", "13:35:33", "+0000");

        Assert.assertEquals(date1, date2);
    }

    @Test
    public void compareTest() throws Exception {
        TimeType time1 = TimeType.valueOf("2013-06-01");
        TimeType time2 = TimeType.valueOf("2013-06-22");

        Assert.assertTrue(time1.compareTo(time2) == -1);
    }
}
