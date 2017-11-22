package com.example.dr.kalkulator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by DR on 20.11.2017.
 */

public class Calculator {
    private static Stack<Double> numbers;
    private static Stack<String> operators;

    public static void setStack() {
        numbers = new Stack<>();
        operators = new Stack<>();
    }

    public static Double calculate(String exp) {
        numbers.clear();
        operators.clear();
        for (String item : convertExpToStringList(exp)) {
            if (item.length() == 1 && OperatorEnum.isOperatorWithBracket(item.charAt(0))) {
                if (!operators.isEmpty()) {
                    String operstorsFromStack = operators.pop();
                    operators.push(operstorsFromStack);
                    if (!OperatorEnum.higherPriority(item, operstorsFromStack)){
                        if(OperatorEnum.CLOSING_BRACKET.toCharacter().equals(item.charAt(0)))
                            operators.push(item);
                        calc();
                    }
                    if(!OperatorEnum.CLOSING_BRACKET.toCharacter().equals(item.charAt(0))
                            || OperatorEnum.OPENING_BRACKET.toCharacter().equals(operstorsFromStack.charAt(0)))
                        operators.push(item);
                } else {
                    operators.push(item);
                }
            } else {
                numbers.push(Double.valueOf(item));
            }
        }
        while (!operators.isEmpty()) {
            calc();
        }
        return numbers.pop();
    }

    private static void calc() {
        OperatorEnum operator = OperatorEnum.valueOf2(operators.pop().charAt(0));
        Double result = 0d;
        switch (operator) {
            case ADDITION:
                result = numbers.pop() + numbers.pop();
                break;
            case SUBTRACTION:
                result = numbers.pop();
                result = numbers.pop() - result;
                break;
            case MULTIPLICATION:
                result = numbers.pop() * numbers.pop();
                break;
            case DIVISION:
                result = numbers.pop();
                result = numbers.pop() / result;
                break;
            case CLOSING_BRACKET:
                String operstorsFromStack = operators.pop();
                while (!(OperatorEnum.OPENING_BRACKET.toCharacter().equals(operstorsFromStack.charAt(0)))) {
                    operators.push(operstorsFromStack);
                    calc();
                    operstorsFromStack = operators.pop();
                }
                break;
            case OPENING_BRACKET:
                break;
            default:
                Log.d("calc", "Brak operatora.Błąd obliczenia.");
                break;
        }
        if(!operator.equals(OperatorEnum.CLOSING_BRACKET))
            numbers.push(result);
    }

    private static List<String> convertExpToStringList(String exp) {
        exp = exp.replace(",", ".");
        List<String> stringList = new ArrayList<>();
        StringBuilder item = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            Character c = exp.charAt(i);
            if (OperatorEnum.isOperatorWithBracket(c)) {
                if (i == 0 && c.equals(OperatorEnum.SUBTRACTION.toCharacter())) {
                    item.append(c);
                } else if (c.equals(OperatorEnum.SUBTRACTION.toCharacter())
                        && OperatorEnum.isOperatorWithBracket(exp.charAt(i - 1))) {
                    item.append(c);
                } else {
                    if (item.length() > 0)
                        stringList.add(item.toString());
                    stringList.add(c.toString());
                    item.setLength(0);
                }
            } else {
                item.append(c);
                if (i == exp.length() - 1)
                    stringList.add(item.toString());
            }
        }
        return stringList;
    }
}
