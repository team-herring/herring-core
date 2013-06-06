package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.DateTimeType;
import org.herring.core.manager.query.types.TimeRangeType;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

/**
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimeRangeTypeTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCreateCondition() throws Exception {
        exception.expect(IllegalArgumentException.class);
        TimeRangeType range = new TimeRangeType(DateTimeType.valueOf("2013-05-30"), DateTimeType.valueOf("2013-05-29"));
    }
}
