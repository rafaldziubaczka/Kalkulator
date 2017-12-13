package com.example.dr.kalkulator;

/**
 * Created by DR on 11.12.2017.
 */

public class Result {
    public static final String TABLE = "Result";

    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_value = "value";

    private Long id;
    private String name;
    private String value;

    public Result(){
    }

    public Result(String name, String value){
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
