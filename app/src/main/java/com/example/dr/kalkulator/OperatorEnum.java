package com.example.dr.kalkulator;

import android.support.annotation.Nullable;

/**
 * Created by DR on 18.11.2017.
 */

public enum OperatorEnum {
    ADDITION("+", '+', 1, 2, false),
    SUBTRACTION("-", '-', 1, 2, false),
    MULTIPLICATION("×", '×', 2, 2, false),
    DIVISION("÷", '÷', 2, 2, false),
    CLOSING_BRACKET(")", ')', 1, 0, false),
    OPENING_BRACKET("(", '(', 0, 0, false),
    LOGARITHM_N("ln", 'n', 3, 1, true),
    LOGARITHM("log", 'l', 3, 1, true),
    SIN("sin", 's', 3, 1, true),
    COS("cos", 'c', 3, 1, true),
    TAN("tan", 't', 3, 1, true),
    FACTORIAL("!", '!', 4, 1, false),
    POWER("^", '^', 4, 2, true),
    SQUARE_ROOT("√", '√', 3, 1, true),
    PERCENT("%", '%', 4, 2, false),
    DEFAULT(" ", ' ', -1, 0, false);

    private final String operator;
    private final Character tag;
    private final int priority;
    private final int required;
    private final boolean withOpeningBracket;

    OperatorEnum(String operator, Character tag, int priority, int required, boolean withOpeningBracket) {
        this.operator = operator;
        this.tag = tag;
        this.priority = priority;
        this.required = required;
        this.withOpeningBracket = withOpeningBracket;
    }

    public static OperatorEnum valueOf2(Character value) {
        for (OperatorEnum o : OperatorEnum.values())
            if (o.tag.equals(value))
                return o;
        return DEFAULT;
    }

    public static boolean isOperator(Character value) {
        for (OperatorEnum o : OperatorEnum.values())
            if (o.tag.equals(value))
                if (!value.equals(OPENING_BRACKET.toCharacter()) && !value.equals(CLOSING_BRACKET.toCharacter())
                        && !value.equals(PERCENT.toCharacter()) && !value.equals(FACTORIAL.toCharacter()))
                    return true;
        return false;
    }

    public static boolean isOperatorWithBracket(Character value) {
        for (OperatorEnum o : OperatorEnum.values())
            if (o.tag.equals(value))
                return true;
        return false;
    }

    public static boolean higherPriority(String o1, String o2) {
        OperatorEnum operator1 = valueOf2(o1.charAt(0));
        OperatorEnum operator2 = valueOf2(o2.charAt(0));
        if (operator1.equals(OPENING_BRACKET)) {
            return true;
        }
        return operator1.priority > operator2.priority;
    }

    public static boolean deleteOperator(Character operator){
        return valueOf2(operator).isWithOpeningBracket();
    }

    public boolean isWithOpeningBracket() {
        return withOpeningBracket;
    }

    public Character toCharacter() {
        return tag;
    }

    public String toPrint() {
        return operator;
    }
}
