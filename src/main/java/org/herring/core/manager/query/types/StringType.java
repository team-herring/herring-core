package org.herring.core.manager.query.types;

import java.io.Serializable;

/**
 * 쿼리에서 사용하는 문자열 리터럴을 대표하는 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class StringType extends BaseType<String> implements Serializable {

    private static final long serialVersionUID = 4489752017259635554L;

    public StringType(String value) {
        super(String.class, value);
    }

    @Override
    protected String copyValueInstance(String value) {
        return value;
    }

    public static StringType valueOf(String value) {
        return new StringType(value);
    }

    @Override
    public String toString() {
        return "[<StringType> value=" + getValue() + "]";
    }
}
