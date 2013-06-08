package org.herring.core.manager.query.types;

/**
 * 쿼리 내부의 필드(\@로 시작하는 부분들)를 대표하는 타입 클래스.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class FieldType extends BaseType<String> {

    private static final long serialVersionUID = -1921086639815432528L;

    public FieldType(String value) {
        super(String.class);
        setValue(value);
    }

    @Override
    protected String copyValueInstance(String value) {
        return value;
    }

    public static FieldType valueOf(String value) {
        return new FieldType(value);
    }

    public void setValue(String value) {
        if (value.charAt(0) != '@' || value.indexOf('@') != value.lastIndexOf('@'))
            throw new IllegalArgumentException("올바른 변수 이름 형식이 아닙니다.");

        super.setValue(value);
    }

    @Override
    public String toString() {
        return "[<FieldType> value=" + getValue() + "]";
    }
}
