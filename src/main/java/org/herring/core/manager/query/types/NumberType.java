package org.herring.core.manager.query.types;

/**
 * 쿼리에서 수치형을 대표하는 데이터 타입 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class NumberType extends BaseType<Double> implements Comparable<NumberType> {

    private static final long serialVersionUID = 4741968496907818223L;

    public NumberType(double value) {
        super(Double.class, value);
    }

    @Override
    protected Double copyValueInstance(Double value) {
        return value;
    }

    @Override
    public int compareTo(NumberType o) {
        Double a = getValue();
        Double b = o.getValue();

        return a.compareTo(b);
    }

    public static NumberType valueOf(String value) {
        return new NumberType(Double.valueOf(value));
    }

    @Override
    public String toString() {
        return "[<NumberType> value=" + getValue().toString() + "]";
    }
}
