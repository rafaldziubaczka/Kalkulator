package com.example.dr.kalkulator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.Stack;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private ResultRepo resultRepo;
    private HistoryRepo historyRepo;

    StringBuilder exp;
    TextView expression;
    TextView result;

    Double score;

    boolean lastCharIsOperator = false;
    boolean isCommaInLastValue = false;
    boolean isScientificNotation = false;

    Button addition;
    Button subtraction;
    Button multiplication;
    Button division;
    Button comma;
    Button closingBracket;
    Button openingBracket;
    Button pow;

    Button rad;

    int closingBracketCount = 0;
    int openingBracketCount = 0;

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultRepo = new ResultRepo(this);
        historyRepo = new HistoryRepo(this);
        setContentView(R.layout.activity_main);
        expression = (TextView) findViewById(R.id.expression);
        result = (TextView) findViewById(R.id.result);
        exp = new StringBuilder("0");
        Calculator.setStack();
        setButtons();
        updateExpression();
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
    }

    public void onClickOperator(View view) {
        if (((Character) exp.charAt(exp.length() - 1)).equals(',')) {
            exp.append("0");
        } else if (((Character) exp.charAt(exp.length() - 1)).equals('E')) {
            exp.append("1");
        }
        Button b = (Button) view;
        switch (OperatorEnum.valueOf2(b.getTag().toString().charAt(0))) {
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
                        || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET)
                        || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.PERCENT)
                        || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.FACTORIAL))
                    exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
                exp.append(OperatorEnum.OPENING_BRACKET.toCharacter());
                openingBracketCount++;
                break;
            case POWER:
                checkLastChar(OperatorEnum.POWER);
                exp.append(OperatorEnum.POWER.toCharacter());
                exp.append(OperatorEnum.OPENING_BRACKET.toCharacter());
                openingBracketCount++;
                break;
            case SQUARE_ROOT:
                setFunctionsOperator(OperatorEnum.SQUARE_ROOT);
                break;
            case PERCENT:
                if (!OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))
                        || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET)) {
                    exp.append(OperatorEnum.PERCENT.toCharacter());
                }
                break;
            case FACTORIAL:
                if (!OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))
                        || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET)) {
                    exp.append(OperatorEnum.FACTORIAL.toCharacter());
                }
                break;
            case LOGARITHM:
                setFunctionsOperator(OperatorEnum.LOGARITHM);
                break;
            case LOGARITHM_N:
                setFunctionsOperator(OperatorEnum.LOGARITHM_N);
                break;
            case SIN:
                setFunctionsOperator(OperatorEnum.SIN);
                break;
            case COS:
                setFunctionsOperator(OperatorEnum.COS);
                break;
            case TAN:
                setFunctionsOperator(OperatorEnum.TAN);
                break;
            case ABSOLUTE_VALUE:
                setFunctionsOperator(OperatorEnum.ABSOLUTE_VALUE);
                break;
            default:
                Log.d("onClickOperator", "Brak operatora.");
                break;
        }
        isCommaInLastValue = false;
        lastCharIsOperator = true;
        isScientificNotation = false;
        updateExpression();
    }

    public void onClickNumber(View view) {
        Button b = (Button) view;
        if (exp.length() == 1 && exp.toString().equals("0")) {
            exp.setLength(0);
            if (b.getTag() != null && b.getTag().equals("E") && !isScientificNotation)
                exp.append("1");
        } else if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET.toCharacter())) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        } else if (((Character) exp.charAt(exp.length() - 1)).equals('π') || ((Character) exp.charAt(exp.length() - 1)).equals('e')) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        } else if (!OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))
                && (((Character) b.getText().charAt(0)).equals('π') || ((Character) b.getText().charAt(0)).equals('e') || ((Character) b.getText().charAt(0)).equals('E'))) {
            if (((Character) exp.charAt(exp.length() - 1)).equals('E')) {
                exp.append("1");
            }
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        }
        if (exp.length() > 0 && OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.PERCENT)) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        }
        if (exp.length() > 0 && OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.FACTORIAL)) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        }
        if (exp.length() > 0 && OperatorEnum.MULTIPLICATION.toCharacter().equals(((Character) exp.charAt(exp.length() - 1)))) {
            isScientificNotation = false;
        }
        if (b.getTag() != null && b.getTag().equals("E") && !isScientificNotation) {
            if (OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1)))
                exp.append("1");
            exp.append(b.getTag());
            isScientificNotation = true;
        } else {
            exp.append(b.getText());
        }
        lastCharIsOperator = false;
        updateExpression();
    }

    public void onClickClear(View view) {
        exp.setLength(0);
        exp.append("0");
        isCommaInLastValue = false;
        lastCharIsOperator = false;
        isScientificNotation = false;
        openingBracketCount = 0;
        closingBracketCount = 0;
        updateExpression();
    }

    public void onClickBack(View view) {
        if (exp.substring(exp.length() - 1).equals(",")) {
            isCommaInLastValue = false;
        } else if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.OPENING_BRACKET.toCharacter())) {
            openingBracketCount--;
        } else if (((Character) exp.charAt(exp.length() - 1)).equals('E')) {
            isScientificNotation = false;
        }
        if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET.toCharacter())) {
            closingBracketCount--;
        }
        exp.deleteCharAt(exp.length() - 1);
        if (exp.length() == 0) {
            exp.append("0");
        } else if (OperatorEnum.isOperator(exp.charAt(exp.length() - 1))) {
            isCommaInLastValue = false;
            if (OperatorEnum.deleteOperator(exp.charAt(exp.length() - 1))) {
                exp.deleteCharAt(exp.length() - 1);
            }
        } else {
            for (int i = exp.length(); i > 1; i--) {
                Character s = exp.charAt(i - 1);
                if (s.equals(',')) {
                    isCommaInLastValue = true;
                } else if (s.equals('E')) {
                    isScientificNotation = true;
                } else if (OperatorEnum.isOperator(s)) {
                    isCommaInLastValue = false;
                    isScientificNotation = false;
                    break;
                }
                if (isCommaInLastValue && isScientificNotation)
                    break;
            }
        }
        if (exp.length() == 0) {
            exp.append("0");
        }
        updateExpression();
    }

    public  void addSaveValue(String saveValue){
        if (exp.length() == 1 && exp.toString().equals("0")) {
            exp.setLength(0);
        } else if (((Character) exp.charAt(exp.length() - 1)).equals('π') || ((Character) exp.charAt(exp.length() - 1)).equals('e')) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        } else if (!OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1)) || OperatorEnum.CLOSING_BRACKET.toCharacter().equals(exp.charAt(exp.length() - 1))){
            if (((Character) exp.charAt(exp.length() - 1)).equals(',')) {
                exp.append("0");
            } else if (((Character) exp.charAt(exp.length() - 1)).equals('E')) {
                exp.append("1");
            }
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        }
        for (int i = saveValue.length(); i > 1; i--) {
            Character s = saveValue.charAt(i - 1);
            if (s.equals(',')) {
                isCommaInLastValue = true;
            } else if (s.equals('E')) {
                isScientificNotation = true;
            }
            if (isCommaInLastValue && isScientificNotation)
                break;
        }
        exp.append(saveValue);
        updateExpression();
    }

    public void onClickComma(View view) {
        if (OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))) {
            if (((Character) exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET.toCharacter())
                    || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.PERCENT)
                    || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.FACTORIAL))
                exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
            exp.append("0");
        } else if (((Character) exp.charAt(exp.length() - 1)).equals('π') || ((Character) exp.charAt(exp.length() - 1)).equals('e')) {
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
            exp.append("0");
        }
        if (!isCommaInLastValue && !isScientificNotation) {
            exp.append(",");
            isCommaInLastValue = true;
        }
        updateExpression();
    }

    public void onClickResult(View view) {
        updateResult(true);
        expression.setText(convertToView(exp.toString()));
    }

    public void checkLastChar(OperatorEnum operator) {
        if (OperatorEnum.isOperator(exp.charAt(exp.length() - 1))) {
            if ((!OperatorEnum.SUBTRACTION.toCharacter().equals(exp.charAt(exp.length() - 1))
                    && !OperatorEnum.SUBTRACTION.equals(operator)) || exp.length() == 1) {
                exp.deleteCharAt(exp.length() - 1);
            } else if ((!OperatorEnum.isOperator(exp.charAt(exp.length() - 2))
                    && !OperatorEnum.SUBTRACTION.equals(operator)) || OperatorEnum.OPENING_BRACKET.toCharacter().equals(exp.charAt(exp.length() - 2))) {
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
        } else if (exp.length() == 1 && ((Character) exp.charAt(exp.length() - 1)).equals('0')) {
            exp.setLength(0);
            exp.append("0");
        }
    }

    public void updateExpression() {
        updateResult(false);
        expression.setText(convertToView(exp.toString()));

//        addition.setEnabled(!lastCharIsOperator);
//        subtraction.setEnabled(!lastCharIsOperator);
//        multiplication.setEnabled(!lastCharIsOperator);
//        division.setEnabled(!lastCharIsOperator);

//        comma.setEnabled(!isCommaInLastValue);
    }

    public void updateResult(boolean result) {
        StringBuilder stringExp = new StringBuilder(exp.toString().replace("-(",
                OperatorEnum.SUBTRACTION.toPrint() + "1" + OperatorEnum.MULTIPLICATION.toPrint() + OperatorEnum.OPENING_BRACKET.toPrint()));
        int localOpeningBracketCount = openingBracketCount;
        if (OperatorEnum.isOperatorWithBracket(stringExp.charAt(stringExp.length() - 1))) {
            while (OperatorEnum.isOperatorWithBracket(stringExp.charAt(stringExp.length() - 1))
                    && !OperatorEnum.CLOSING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))
                    && !OperatorEnum.PERCENT.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))
                    && !OperatorEnum.FACTORIAL.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))) {
                if (OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1)))
                    localOpeningBracketCount--;
                stringExp.deleteCharAt(stringExp.length() - 1);
                if (stringExp.length() == 0)
                    stringExp.append("0");
            }

//            if (OperatorEnum.isOperator(stringExp.charAt(stringExp.length() - 1))) {
//                stringExp.deleteCharAt(stringExp.length() - 1);
//            } else if (OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))) {
//                stringExp.append("0");
//            }
//        } else if (OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1))) {
//            while(OperatorEnum.isOperatorWithBracket(stringExp.charAt(stringExp.length() - 1))){
//                if(OperatorEnum.OPENING_BRACKET.toCharacter().equals(stringExp.charAt(stringExp.length() - 1)))
//                    localOpeningBracketCount--;
//                stringExp.deleteCharAt(stringExp.length() - 1);
//                if (stringExp.length() == 0)
//                    stringExp.append("0");
//            }
//            if (stringExp.length() > 2)
//                stringExp.deleteCharAt(stringExp.length() - 1);
        } else if (((Character) stringExp.charAt(stringExp.length() - 1)).equals(',')) {
            stringExp.append("0");
        } else if (((Character) stringExp.charAt(stringExp.length() - 1)).equals('E')) {
            stringExp.append("1");
        }

        int count = localOpeningBracketCount - closingBracketCount;
        while (count > 0) {
            stringExp.append(OperatorEnum.CLOSING_BRACKET.toCharacter());
            count--;
        }
        score = Calculator.calculate(stringExp.toString());
        this.result.setText(score.toString());
        if (result) {
            exp = stringExp;
            closingBracketCount = openingBracketCount;
//            resultRepo.insert(new Result("result: " + score.toString(), score));
            historyRepo.insertWithoutSame(new History(convertToView(exp.toString()), score.toString()));
        }
    }

    public void setFunctionsOperator(OperatorEnum operator) {
        if (exp.length() == 1 && exp.toString().equals("0"))
            exp.setLength(0);
        else if (!OperatorEnum.isOperatorWithBracket(exp.charAt(exp.length() - 1))
                || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.CLOSING_BRACKET)
                || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.PERCENT)
                || OperatorEnum.valueOf2(exp.charAt(exp.length() - 1)).equals(OperatorEnum.FACTORIAL))
            exp.append(OperatorEnum.MULTIPLICATION.toCharacter());
        exp.append(operator.toCharacter());
        exp.append(OperatorEnum.OPENING_BRACKET.toCharacter());
        openingBracketCount++;
    }

    public void setButtons() {
        addition = (Button) findViewById(R.id.bAddition);
        subtraction = (Button) findViewById(R.id.bSubtraction);
        multiplication = (Button) findViewById(R.id.bMultiplication);
        division = (Button) findViewById(R.id.bDivision);
        comma = (Button) findViewById(R.id.bComma);
        closingBracket = (Button) findViewById(R.id.bClosingBracket);
        openingBracket = (Button) findViewById(R.id.bOpeningBracket);
        pow = (Button) findViewById(R.id.ButtonPow);
        pow.setText(Html.fromHtml("x<sup><small>y</small></sup>"));
        rad = (Button) findViewById(R.id.bRad);
    }

    public void onClickHistory(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View vHistory = getLayoutInflater().inflate(R.layout.history, null);
        ListView listHistory = (ListView) vHistory.findViewById(R.id.listHistory);
        listHistory.setAdapter(new ListHistoryAdapter(this, R.layout.list_history_item, historyRepo.getHistoryList()));
        builder.setView(vHistory);
        final AlertDialog alertDialogHistory = builder.create();
        alertDialogHistory.show();
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogHistory.dismiss();
            }
        });
    }

    public void onClickSave(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View vSave = getLayoutInflater().inflate(R.layout.save, null);
        ListView listSave = (ListView) vSave.findViewById(R.id.listSave);
        listSave.setAdapter(new ListSaveAdapter(this, R.layout.list_save_item, resultRepo.getResultList()));
        Button saveButton = (Button)vSave.findViewById(R.id.bSaveValue);
        builder.setView(vSave);
        final AlertDialog alertDialogSave = builder.create();
        alertDialogSave.show();
        listSave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = ((TextView) view.findViewById(R.id.ItemSaveValue)).getText().toString();
                addSaveValue(value.replace(".",","));
                alertDialogSave.dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                final View vSaveSave = getLayoutInflater().inflate(R.layout.save_value, null);
                TextView v1 = (TextView)vSaveSave.findViewById(R.id.saveValue);
                v1.setText(score.toString());
                final Button saveButton = (Button)vSaveSave.findViewById(R.id.bSaveSaveValue);
                builder1.setView(vSaveSave);
                final AlertDialog alertDialogSaveSave = builder1.create();
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText et = (EditText)vSaveSave.findViewById(R.id.saveValueName);
                        String valueName = score.toString();
                        if(!et.getText().toString().isEmpty())
                            valueName = et.getText().toString();
                        resultRepo.insertWithCheck(new Result(valueName,score.toString()));
                        Toast.makeText(getApplicationContext(),"Save value: "+valueName,Toast.LENGTH_SHORT).show();
                        alertDialogSave.dismiss();
                        alertDialogSaveSave.dismiss();
                    }
                });
                alertDialogSaveSave.show();
            }
        });
        Button saveBack = (Button)vSave.findViewById(R.id.bBackSave);
        saveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogSave.dismiss();
            }
        });
    }

    public void onClickFunctions(View view) {
        viewFlipper.setDisplayedChild((viewFlipper.indexOfChild(findViewById(R.id.LFunctions))));
    }

    public void onClickNumbers(View view) {
        viewFlipper.setDisplayedChild((viewFlipper.indexOfChild(findViewById(R.id.LNumbers))));
    }

    private String convertToView(String expView) {
        for (OperatorEnum o : OperatorEnum.values()) {
            expView = expView.replaceAll(Pattern.quote(o.toCharacter().toString()), o.toPrint());
        }
        return expView;
    }

    public void onClickChangeRad(View view) {
        Calculator.setRad(!Calculator.isRad());
        if (!Calculator.isRad()) {
            rad.setText("Rad");
        } else {
            rad.setText("Deg");
        }
        updateExpression();
    }
}