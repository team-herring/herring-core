package org.herring.core.manager.query;

import org.herring.core.manager.query.types.BaseType;

/**
 * 이 클래스는 현재 데크 최하위에 위치한 데이터 셋에 Filter 기능을 수행하는 명령 클래스다.
 * 이 명령이 수행된 후, 데크 최상단에는 기본 데이터 셋에 Filter를 적용한 데이터 셋이 존재해야 한다.
 *
 * @author Chiwan Park
 * @since 0.1
 */
public class FilterCommand extends QueryCommand {

    private static final long serialVersionUID = 5489643679808535404L;

    /**
     * 데이터 셋에 수행할 Filter 연산을 나타내는 상수다.
     * <p/>
     * EQ: 특정 값과 특정 필드가 동일한 값인 데이터만 선택하는 Filter다.
     * NEQ: 특정 값과 특정 필드가 다른 값인 데이터만 선택하는 Filter다.
     * GT: 특정 필드가 특정 값보다 큰 데이터만 선택하는 Filter다.
     * GTE: 특정 필드가 특정 값보다 크거나 같은 데이터만 선택하는 Filter다.
     * LT: 특정 필드가 특정 값보다 작은 데이터만 선택하는 Filter다.
     * LTE: 특정 필드가 특정 값보다 작거나 같은 데이터만 선택하는 Filter다.
     * IN: 필드 형태가 문자열인 경우에 한해 적용 가능하며, 주어진 값이 특정 필드의 부분 문자열인 데이터만 선택하는 Filter다.
     * NOT_IN: 필드 형태가 문자열인 경우에 한해 적용가능하며, 주어진 값이 특정 필드의 부분 문자열이 아닌 데이터만 선택하는 Filter다.
     */
    public enum Operator {
        EQ, NEQ, GT, GTE, LT, LTE, IN, NOT_IN
    }

    private BaseType operand1;
    private BaseType operand2;
    private Operator operator;

    public FilterCommand(BaseType operand1, BaseType operand2, Operator operator) {
        super(CommandType.FILTER);

        setOperator(operator);
        setOperand1(operand1);
        setOperand2(operand2);
    }

    /**
     * 데이터 셋에 수행할 Filter 연산의 종류를 받아온다.
     *
     * @return Filter 연산을 나타내는 상수
     */
    public Operator getOperator() {
        return operator;
    }

    private void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Filter 수식의 피연산자 중 첫 번째를 돌려준다.
     *
     * @return 피연산자 정보
     */
    public BaseType getOperand1() {
        return operand1;
    }

    private void setOperand1(BaseType operand1) {
        this.operand1 = operand1;
    }

    /**
     * Filter 수식의 피연산자 중 두 번째를 돌려준다.
     *
     * @return 피연산자 정보
     */
    public BaseType getOperand2() {
        return operand2;
    }

    private void setOperand2(BaseType operand2) {
        this.operand2 = operand2;
    }
}
