package com.example.dr.kalkulator;

import android.util.Log;

import org.apache.commons.math3.special.Gamma;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by DR on 20.11.2017.
 */

public class Calculator {
    private static Stack<Double> numbers;
    private static Stack<String> operators;

    private static boolean rad;

    public static void setStack() {
        numbers = new Stack<>();
        operators = new Stack<>();
        rad = true;
    }


    public static boolean isRad() {
        return rad;
    }

    public static void setRad(boolean rad) {
        Calculator.rad = rad;
    }

    public static Double calculate(String exp) {
        numbers.clear();
        operators.clear();
        for (String item : convertExpToStringList(exp)) {
            if (item.length() == 1 && OperatorEnum.isOperatorWithBracket(item.charAt(0))) {
                if (!operators.isEmpty()) {
                    String operstorsFromStack = operators.peek();
                    if (!OperatorEnum.higherPriority(item, operstorsFromStack)) {
                        if (OperatorEnum.CLOSING_BRACKET.toCharacter().equals(item.charAt(0)))
                            operators.push(item);
                        calc();
                    }
                    if (!OperatorEnum.CLOSING_BRACKET.toCharacter().equals(item.charAt(0))
                            || OperatorEnum.OPENING_BRACKET.toCharacter().equals(operstorsFromStack.charAt(0))) {
                        operators.push(item);
                        if (OperatorEnum.CLOSING_BRACKET.toCharacter().equals(item.charAt(0)))
                            calc();
                    }
                } else {
                    operators.push(item);
                }
            } else {
                if (item.equals("π")) {
                    numbers.push(Math.PI);
                } else if (item.equals("e")) {
                    numbers.push(Math.E);
                } else {
                    numbers.push(Double.valueOf(item));
                }
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
            case POWER:
                result = numbers.pop();
                result = Math.pow(numbers.pop(), result);
                break;
            case SQUARE_ROOT:
                result = Math.sqrt(numbers.pop());
                break;
            case PERCENT:
                if (numbers.size() == 1) {
                    result = numbers.pop() / 100;
                } else {
                    double perc = numbers.pop();
                    double number = numbers.peek();
                    result = number * perc / 100;
                }
                break;
            case FACTORIAL:
                result = Gamma.gamma(numbers.pop() + 1);
                break;
            default:
                Log.d("calc", "Brak operatora.Błąd obliczenia.");
                break;
        }
        if (!operator.equals(OperatorEnum.CLOSING_BRACKET))
            numbers.push(result);
    }

    private static double factorial(double value) {
        double result = value - Math.floor(value) + 1;
        for (; value > 1; value -= 1) {
            result *= value;
        }
        return result;
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
