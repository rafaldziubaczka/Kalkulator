package com.example.dr.kalkulator;

import android.support.annotation.Nullable;

/**
 * Created by DR on 18.11.2017.
 */

public enum OperatorEnum {
    ADDITION('+', 1, 2),
    SUBTRACTION('-', 1, 2),
    MULTIPLICATION('×', 2, 2),
    DIVISION('÷', 2, 2),
    CLOSING_BRACKET(')', 1, 0),
    OPENING_BRACKET('(', 0, 0),
    DEFAULT(' ', -1, 0);

    private final Character operator;
    private final int priority;
    private final int required;

    OperatorEnum(Character operator, int priority, int required) {
        this.operator = operator;
        this.priority = priority;
        this.required = required;
    }

    public static OperatorEnum valueOf2(Character value) {
        for (OperatorEnum o : OperatorEnum.values())
            if (o.operator.equals(value))
                return o;
        return DEFAULT;
    }

    public static boolean isOperator(Character value) {
        for (OperatorEnum o : OperatorEnum.values())
            if (o.operator.equals(value))
                if (!value.equals(OPENING_BRACKET.toCharacter()) && !value.equals(CLOSING_BRACKET.toCharacter()))
                    return true;
        return false;
    }

    public static boolean isOperatorWithBracket(Character value) {
        for (OperatorEnum o : OperatorEnum.values())
            if (o.operator.equals(value))
                return true;
        return false;
    }

    public static boolean higherPriority(String o1, String o2) {
        OperatorEnum operator1 = valueOf2(o1.charAt(0));
        OperatorEnum operator2 = valueOf2(o2.charAt(0));
        if(operator1.equals(OPENING_BRACKET)){
            return true;
        }
        return operator1.priority > operator2.priority;
    }

    public Character toCharacter() {
        return operator;
    }

}
