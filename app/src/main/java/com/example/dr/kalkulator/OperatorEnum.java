package com.example.dr.kalkulator;

import android.support.annotation.Nullable;

/**
 * Created by DR on 18.11.2017.
 */

public enum OperatorEnum {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("×"),
    DIVISION("÷"),
    CLOSING_BRACKET(")"),
    OPENING_BRACKET("("),
    DEFAULT("");

    private final String operator;

    OperatorEnum(String operator) {
        this.operator = operator;
    }

    public static OperatorEnum valueOf2(String value){
        for(OperatorEnum o : OperatorEnum.values())
            if(o.operator.equals(value))
                return o;
        return DEFAULT;
    }

    public static boolean isOperator(String value){
        for(OperatorEnum o : OperatorEnum.values())
            if(o.operator.equals(value))
                return true;
        return false;
    }


    @Override
    public String toString() {
        return operator;
    }
}
