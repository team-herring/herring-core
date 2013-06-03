package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.DateTimeType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
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
    public void compareTest() throws Exception {
        DateTimeType time1 = DateTimeType.valueOf("2013-06-01");
        DateTimeType time2 = DateTimeType.valueOf("2013-06-22");

        Assert.assertTrue(time1.compareTo(time2) == -1);
    }
}
