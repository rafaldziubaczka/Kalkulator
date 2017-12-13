package com.example.dr.kalkulator;

/**
 * Created by DR on 11.12.2017.
 */

public class History {
    public static final String TABLE = "History";

    public static final String KEY_ID = "id";
    public static final String KEY_expression = "expression";
    public static final String KEY_result = "result";

    private Long id;
    private String expression;
    private String result;

    public History(){
    }

    public History(String expression, String result){
        this.expression = expression;
        this.result = result;
    }

    public boolean equals(History history) {
        if(this.expression.equals(history.expression) && this.result.equals(history.result))
            return true;
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
