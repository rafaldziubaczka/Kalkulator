package com.example.dr.kalkulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Stack;

public class MainActivity extends AppCompatActivity{

    StringBuilder exp;
    TextView expression;
    TextView result;

    boolean lastCharIsOperator = false;
    boolean isCommaInLastValue = false;

    Button addition;
    Button subtraction;
    Button multiplication;
    Button division;
    Button comma;
    Button closingBracket;
    Button openingBracket;
    Button pow;

    int closingBracketCount = 0;
    int openingBracketCount = 0;

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expression = (TextView) findViewById(R.id.expression);
        result = (TextView) findViewById(R.id.result);
        exp = new StringBuilder("0");
        Calculator.setStack();
        setButtons();
        updateExpression();
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
    }

    public void onClickOperator(View view) {
        if (exp.substring(exp.length() - 1).equals(",")) {
            exp.append("0");
        }
        Button b = (Button) view;
        switch (OperatorEnum.valueOf2(b.getText().charAt(0))) {
            case ADDITION:
                checkLastChar(OperatorEnum.ADDITION);
                exp.append(OperatorEnum.ADDITION.toCharacter());
                break;
            case SUBTRACTION:
                checkLastChar(OperatorEnum.SUBTRACTION);
                exp.append(OperatorEnum.SUBTRACTION.toCharacter());
                break;
            case MULTIPLICATION:
                checkLastChar(OperatorEnum.MULTIPLICATION);
                exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
                break;
            case DIVISION:
                checkLastChar(OperatorEnum.DIVISION);
                exp.append(OperatorEnum.DIVISION.toCharacter());
                break;
            case CLOSING_BRACKET:
                if (openingBracketCount > closingBracketCount) {
                    if (OperatorEnum.isOperator(exp.charAt(exp.length() - 1))
                            || ((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.OPENING_BRACKET.toCharacter())) {
                        exp.append("0");
                    }
                    exp.append(OperatorEnum.CLOSING_BRACKET.toCharacter());
                    closingBracketCount++;
                }
                break;
            case OPENING_BRACKET:
                if (exp.length() == 1 && exp.toString().equals("0"))
                    exp.setLength(0);
                else if (!OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))
                        || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET))
                    exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
                exp.append(OperatorEnum.OPENING_BRACKET.toCharacter());
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
        } else if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET.toCharacter())) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
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
        openingBracketCount = 0;
        closingBracketCount = 0;
        updateExpression();
//        System.gc();
    }

    public void onClickBack(View view) {
        if (exp.substring(exp.length() - 1).equals(",")) {
            isCommaInLastValue = false;
        } else if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.OPENING_BRACKET.toCharacter())) {
            openingBracketCount--;
        }
        if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET.toCharacter())) {
            closingBracketCount--;
        }
        exp.deleteCharAt(exp.length() - 1);
        if (exp.length() == 0) {
            exp.append("0");
        } else if (OperatorEnum.isOperator(exp.charAt(exp.length() - 1))) {
            isCommaInLastValue = false;
        } else {
            for (int i = exp.length(); i > 1; i--) {
                Character s = exp.charAt(i - 1);
                if (s.equals(',')) {
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
        if (OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))) {
            if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET.toCharacter()))
                exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
            exp.append("0");
        }
        if (!isCommaInLastValue) {
            exp.append(",");
        }
        isCommaInLastValue = true;
        updateExpression();
    }

    public void onClickResult(View view) {
        updateResult(true);
        expression.setText(exp);
    }

    public void checkLastChar(OperatorEnum operator) {
        if (OperatorEnum.isOperator(exp.charAt(exp.length() - 1))) {
            if (!OperatorEnum.SUBTRACTION.toCharacter().equals(exp.charAt(exp.length() - 1))
                    && !OperatorEnum.SUBTRACTION.equals(operator)) {
                exp.deleteCharAt(exp.length() - 1);
            } else if (!OperatorEnum.isOperator(exp.charAt(exp.length() - 2))
                    && !OperatorEnum.SUBTRACTION.equals(operator)) {
                exp.deleteCharAt(exp.length() - 1);
            } else if (OperatorEnum.isOperator(exp.charAt(exp.length() - 2))) {
                exp.deleteCharAt(exp.length() - 1);
                exp.deleteCharAt(exp.length() - 1);
            }
        } else if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.OPENING_BRACKET.toCharacter())
                && !OperatorEnum.SUBTRACTION.equals(operator)) {
            exp.append("0");
        } else if (exp.length() == 1 && ((Character) exp.charAt(exp.length() - 1)).equals('0')
                && OperatorEnum.SUBTRACTION.equals(operator)) {
            exp.setLength(0);
        }else if (exp.length() == 1 && ((Character) exp.charAt(exp.length() - 1)).equals('0')) {
            exp.setLength(0);
            exp.append("0");
        }
    }

    public void updateExpression() {
        updateResult(false);
        expression.setText(exp);

//        addition.setEnabled(!lastCharIsOperator);
//        subtraction.setEnabled(!lastCharIsOperator);
//        multiplication.setEnabled(!lastCharIsOperator);
//        division.setEnabled(!lastCharIsOperator);

//        comma.setEnabled(!isCommaInLastValue);
    }

    public void updateResult(boolean result) {
        StringBuilder stringExp = new StringBuilder(exp);
        int localOpeningBracketCount = openingBracketCount;
        if (OperatorEnum.isOperator(stringExp.charAt(stringExp.length() - 1))) {
            stringExp.deleteCharAt(stringExp.length() - 1);
            if (stringExp.length() == 0)
                stringExp.append("0");
            if (OperatorEnum.isOperator(stringExp.charAt(stringExp.length() - 1))) {
                stringExp.deleteCharAt(stringExp.length() - 1);
            } else if (OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))) {
                stringExp.append("0");
            }
        } else if (OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))) {
            while(OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))){
                stringExp.deleteCharAt(stringExp.length() - 1);
                localOpeningBracketCount--;
                if (stringExp.length() == 0)
                    stringExp.append("0");
            }
            if (stringExp.length() > 2)
                stringExp.deleteCharAt(stringExp.length() - 1);
        } else if (((Character) stringExp.charAt(stringExp.length() - 1)).equals(','))
            stringExp.append("0");
        int count = localOpeningBracketCount - closingBracketCount;
        while (count > 0) {
            stringExp.append(OperatorEnum.CLOSING_BRACKET.toCharacter());
            count--;
        }
        this.result.setText(Calculator.calculate(stringExp.toString()).toString());
        if (result){
            exp = stringExp;
            closingBracketCount =openingBracketCount;
        }
    }

    public void setButtons() {
        addition = (Button) findViewById(R.id.bAddition);
        subtraction = (Button) findViewById(R.id.bSubtraction);
        multiplication = (Button) findViewById(R.id.bMultiplication);
        division = (Button) findViewById(R.id.bDivision);
        comma = (Button) findViewById(R.id.bComma);
        closingBracket = (Button) findViewById(R.id.bClosingBracket);
        openingBracket = (Button) findViewById(R.id.bOpeningBracket);
        pow = (Button)findViewById(R.id.ButtonPow);
        pow.setText(Html.fromHtml("x<sup><small>y</small></sup>"));
    }

    public void onClickHistory(View view) {
        viewFlipper.setDisplayedChild((viewFlipper.indexOfChild(findViewById(R.id.LHistory))));
    }

    public void onClickSave(View view) {
        viewFlipper.setDisplayedChild((viewFlipper.indexOfChild(findViewById(R.id.LSave))));
    }

    public void onClickFunctions(View view) {
        viewFlipper.setDisplayedChild((viewFlipper.indexOfChild(findViewById(R.id.LFunctions))));
    }

    public void onClickNumbers(View view) {
        viewFlipper.setDisplayedChild((viewFlipper.indexOfChild(findViewById(R.id.LNumbers))));
    }
}