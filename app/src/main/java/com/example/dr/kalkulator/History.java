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
    private Double result;

    public History(){
    }

    public History(String expression, Double result){
        this.expression = expression;
        this.result = result;
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

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
