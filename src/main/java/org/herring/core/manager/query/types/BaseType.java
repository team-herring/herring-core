package org.herring.core.manager.query.types;

import java.io.Serializable;

/**
 * Herring Query Language에서 사용하는 자료형의 기본 클래스.
 * 모든 자료형은 이 클래스를 상속받아 작성되어야 한다. BaseType을 제외한 모든 자료형은 반드시 Serializable 인터페이스를 구현해야한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public abstract class BaseType<T extends Serializable> {
    private T value;
    private Class<?> typeClass;

    protected BaseType() {
        setTypeClass(Object.class);
    }

    protected BaseType(Class<T> typeClass) {
        setTypeClass(typeClass);
    }

    protected BaseType(Class<T> typeClass, T value) {
        this(typeClass);
        setValue(value);
    }

    protected abstract T copyValueInstance(T value);

    private void setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    public T getValue() {
        return copyValueInstance(value);
    }

    public void setValue(T value) {
        this.value = copyValueInstance(value);
    }

    public String getTypeString() {
        return typeClass.getName();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof BaseType) && getValue().equals(((BaseType) o).getValue());
    }
}
