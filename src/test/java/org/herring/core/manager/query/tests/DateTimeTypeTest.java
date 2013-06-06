package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.DateTimeType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * DateTimeType 클래스의 기본 기능 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateTimeTypeTest {

    @Test
    public void valueOfTest() throws Exception {
        List<DateTimeType> dateTimeList = new ArrayList<DateTimeType>();

        dateTimeList.add(DateTimeType.valueOf("2013-06-03"));
        dateTimeList.add(DateTimeType.valueOf("2013-06-03T00:00:00"));
        dateTimeList.add(DateTimeType.valueOf("2013-06-03T09:00:00Z+0900"));

        for (DateTimeType i : dateTimeList) {
            for (DateTimeType j : dateTimeList) {
                Assert.assertEquals(i, j);
            }
        }
    }

    @Test
    public void valueOfTest2() throws Exception {
        DateTimeType date1 = DateTimeType.valueOf("2013-06-03T13:35:33");
        DateTimeType date2 = DateTimeType.valueOf("2013-06-03", "13:35:33", "+0000");

        Assert.assertEquals(date1, date2);
    }

    @Test
    public void compareTest() throws Exception {
        DateTimeType time1 = DateTimeType.valueOf("2013-06-01");
        DateTimeType time2 = DateTimeType.valueOf("2013-06-22");

        Assert.assertTrue(time1.compareTo(time2) == -1);
    }
}
