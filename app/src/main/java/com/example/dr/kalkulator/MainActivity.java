package com.example.dr.kalkulator;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    StringBuilder exp;
    TextView expression;
    TextView result;
    Stack<Double> numbers;
    Stack<String> operators;

    boolean lastCharIsOperator = false;
    boolean isCommaInLastValue = false;

    Button addition;
    Button subtraction;
    Button multiplication;
    Button division;
    Button comma;
    Button closingBracket;
    Button openingBracket;

    int closingBracketCount = 0;
    int openingBracketCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expression = (TextView) findViewById(R.id.expression);
        result = (TextView) findViewById(R.id.result);
        exp = new StringBuilder("0");
        numbers = new Stack<>();
        operators = new Stack<>();
        setButtons();
        updateExpression();
    }

    public void onClickOperator(View view) {
        if (exp.substring(exp.length() - 1).equals(",")) {
            exp.append("0");
        }
        Button b = (Button) view;
        switch (OperatorEnum.valueOf2(b.getText().toString())) {
            case ADDITION:
                checkLastChar(OperatorEnum.ADDITION);
                exp.append(OperatorEnum.ADDITION.toString());
                break;
            case SUBTRACTION:
                checkLastChar(OperatorEnum.SUBTRACTION);
                exp.append(OperatorEnum.SUBTRACTION.toString());
                break;
            case MULTIPLICATION:
                checkLastChar(OperatorEnum.MULTIPLICATION);
                exp.append(OperatorEnum.MULTIPLICATION.toString());
                break;
            case DIVISION:
                checkLastChar(OperatorEnum.DIVISION);
                exp.append(OperatorEnum.DIVISION.toString());
                break;
            case CLOSING_BRACKET:
                exp.append(OperatorEnum.CLOSING_BRACKET.toString());
                closingBracketCount--;
                break;
            case OPENING_BRACKET:
                if (exp.length() == 1 && exp.toString().equals("0"))
                    exp.setLength(0);
                else if (!OperatorEnum.isOperator(exp.substring(exp.length() - 1))
                        || OperatorEnum.valueOf2(exp.substring(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET))
                    exp.append(OperatorEnum.MULTIPLICATION.toString());
                exp.append(OperatorEnum.OPENING_BRACKET.toString());
                openingBracketCount++;
                break;
            default:
                Log.d("onClickOperator", "Brak operatora.");
                break;
        }
        isCommaInLastValue = false;
        lastCharIsOperator = true;
        updateExpression();
    }

    public void onClickNumber(View view) {
        Button b = (Button) view;
        if (exp.length() == 1 && exp.toString().equals("0")) {
            exp.setLength(0);
        } else if (exp.substring(exp.length() - 1).equals(OperatorEnum.CLOSING_BRACKET.toString())) {
            exp.append(OperatorEnum.MULTIPLICATION);
        }
        exp.append(b.getText());
        lastCharIsOperator = false;
        updateExpression();
    }

    public void onClickClear(View view) {
        exp.setLength(0);
        exp.append("0");
        isCommaInLastValue = false;
        lastCharIsOperator = false;
        updateExpression();
    }

    public void onClickBack(View view) {
        if (exp.substring(exp.length() - 1).equals(",")) {
            isCommaInLastValue = false;
        }
        exp.deleteCharAt(exp.length() - 1);
        if (exp.length() == 0) {
            exp.append("0");
        } else if (OperatorEnum.isOperator(exp.substring(exp.length() - 1))) {
            isCommaInLastValue = false;
        } else {
            for (int i = exp.length(); i > 1; i--) {
                String s = exp.substring(i - 1, i);
                if (s.equals(",")) {
                    isCommaInLastValue = true;
                    break;
                } else if (OperatorEnum.isOperator(s)) {
                    isCommaInLastValue = false;
                    break;
                }
            }
        }
        updateExpression();
    }

    public void onClickComma(View view) {
        if (OperatorEnum.isOperator(exp.substring(exp.length() - 1))) {
            exp.append("0");
        }
        if (!isCommaInLastValue) {
            exp.append(",");
        }
        isCommaInLastValue = true;
        updateExpression();
    }

    public void onClickResult(View view) {
    }

    public void checkLastChar(OperatorEnum operator) {
        if (OperatorEnum.isOperator(exp.substring(exp.length() - 1))) {
            if (!OperatorEnum.SUBTRACTION.toString().equals(exp.substring(exp.length() - 1))
                    && !OperatorEnum.SUBTRACTION.equals(operator)) {
                exp.deleteCharAt(exp.length() - 1);
            } else if (!OperatorEnum.isOperator(exp.substring(exp.length() - 2, exp.length() - 1))
                    && !OperatorEnum.SUBTRACTION.equals(operator)) {
                exp.deleteCharAt(exp.length() - 1);
            } else if (OperatorEnum.isOperator(exp.substring(exp.length() - 2, exp.length() - 1))) {
                exp.deleteCharAt(exp.length() - 1);
                exp.deleteCharAt(exp.length() - 1);
            }
        }
    }

    public void updateExpression() {
        expression.setText(exp);

//        addition.setEnabled(!lastCharIsOperator);
//        subtraction.setEnabled(!lastCharIsOperator);
//        multiplication.setEnabled(!lastCharIsOperator);
//        division.setEnabled(!lastCharIsOperator);

//        comma.setEnabled(!isCommaInLastValue);
    }

    public void setButtons() {
        addition = (Button) findViewById(R.id.bAddition);
        subtraction = (Button) findViewById(R.id.bSubtraction);
        multiplication = (Button) findViewById(R.id.bMultiplication);
        division = (Button) findViewById(R.id.bDivision);
        comma = (Button) findViewById(R.id.bComma);
        closingBracket = (Button) findViewById(R.id.bClosingBracket);
        openingBracket = (Button) findViewById(R.id.bOpeningBracket);
    }
}