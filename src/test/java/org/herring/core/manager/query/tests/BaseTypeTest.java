package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.BaseType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * BaseType 클래스의 기본 기능 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseTypeTest {

    private static BaseType<Integer> TYPE;

    @BeforeClass
    public static void setUp() {
        TYPE = new BaseType<Integer>() {
            @Override
            protected Integer copyValueInstance(Integer value) {
                return value;
            }
        };
    }

    @Test
    public void testEqualsWithNull() {
        Assert.assertEquals(TYPE.equals(null), false);
    }
}
