package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.FieldType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FieldTypeTest {

    @Test
    public void firstCharValidationTest() throws Exception {
        boolean flag = false;

        try {
            FieldType field = new FieldType("hello");
        } catch (IllegalArgumentException e) {
            flag = true;
        }

        Assert.assertTrue(flag);
    }

    @Test
    public void doubleAtCharValidationTest() throws Exception {
        boolean flag = false;

        try {
            FieldType field = new FieldType("@hel@lo");
        } catch (IllegalArgumentException e) {
            flag = true;
        }

        Assert.assertTrue(flag);
    }
}
