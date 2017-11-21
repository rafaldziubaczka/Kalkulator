package com.example.dr.kalkulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by DR on 20.11.2017.
 */

public class Calculator {
    private static Stack<Double> numbers;
    private static Stack<OperatorEnum> operators;

    Calculator() {
        numbers = new Stack<>();
        operators = new Stack<>();
    }

    public static double calculate(String exp) {
        numbers = new Stack<>();
        operators = new Stack<>();
        for (char c : exp.toCharArray()) {

        }
        return 2.1;
    }

//    private List<String> convertExp(String exp) {
//        boolean lastOperator = false;
//        List<String> stringList = new ArrayList<>();
//        for (int i = 0; i < exp.length(); i++) {
//            if (OperatorEnum.isOperator(exp.substring(i,i+1))){
//
//            }
//        }
//    }
}
