package org.herring.core.manager.query.tests;

import org.herring.core.manager.query.types.*;
import org.herring.core.protocol.codec.HerringCodec;
import org.herring.core.protocol.codec.SerializableCodec;
import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Herring Manager가 생성하는 Query Language의 데이터 타입들에 대해서 Serialization이 올바르게 수행됨을 확인하는 테스트 케이스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SerializationTest {

    private static HerringCodec CODEC;

    @BeforeClass
    public static void setUp() {
        CODEC = new SerializableCodec();
    }

    @Test
    public void numberTypeTest() throws Exception {
        NumberType number = new NumberType(19); // reference

        byte[] encoded = CODEC.encode(number); // encode
        NumberType restored = (NumberType) CODEC.decode(encoded); // decode

        Assert.assertEquals(number.getValue(), restored.getValue());
    }

    @Test
    public void stringTypeTest() throws Exception {
        StringType string = new StringType("Hello!");

        byte[] encoded = CODEC.encode(string);
        StringType restored = (StringType) CODEC.decode(encoded);

        Assert.assertEquals(string, restored);
    }

    @Test
    public void dateTimeTypeTest() throws Exception {
        DateTimeType dateTime = DateTimeType.valueOf("2009-05-23");

        byte[] encoded = CODEC.encode(dateTime);
        DateTimeType restored = (DateTimeType) CODEC.decode(encoded);

        Assert.assertEquals(dateTime, restored);
    }

    @Test
    public void fieldTypeTest() throws Exception {
        FieldType field = new FieldType("@sample");

        byte[] encoded = CODEC.encode(field);
        FieldType restored = (FieldType) CODEC.decode(encoded);

        Assert.assertEquals(field, restored);
    }

    @Test
    public void timeRangeTypeTest() throws Exception {
        TimeRangeType timeRange = new TimeRangeType(DateTimeType.valueOf("2013-05-20"), DateTimeType.valueOf("2013-05-30"));

        byte[] encoded = CODEC.encode(timeRange);
        TimeRangeType restored = (TimeRangeType) CODEC.decode(encoded);

        Assert.assertEquals(timeRange, restored);
    }
}
