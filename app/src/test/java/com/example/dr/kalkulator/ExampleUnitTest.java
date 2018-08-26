package com.example.dr.kalkulator;

import android.util.Log;

import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void createdStack() {
        Calculator.setStack();
        assertNotNull(Calculator.getNumbers());
        assertNotNull(Calculator.getOperators());
    }

    @Test
    public void checkResult_ADDITION() {
        Calculator.setStack();
        assertEquals(new Double(4.0), Calculator.calculate("2+2"));
    }

    @Test
    public void checkResult_SUBTRACTION() {
        Calculator.setStack();
        assertEquals(new Double(8.0), Calculator.calculate("10-2"));
    }

    @Test
    public void checkResult_MULTIPLICATION() {
        Calculator.setStack();
        assertEquals(new Double(6.0), Calculator.calculate("2×3"));
    }

    @Test
    public void checkResult_DIVISION() {
        Calculator.setStack();
        assertEquals(new Double(2.0), Calculator.calculate("8÷4"));
    }

    @Test
    public void checkResult_SEQUENCE() {
        Calculator.setStack();
        assertEquals(new Double(6.0), Calculator.calculate("2+2×2"));
    }

    @Test
    public void checkResult_BRACKET() {
        Calculator.setStack();
        assertEquals(new Double(8.0), Calculator.calculate("(2+2)×2"));
    }

    @Test
    public void checkResult_LOGARITHM() { // log(10)
        Calculator.setStack();
        assertEquals(new Double(1.0), Calculator.calculate("l(10)"));
    }

    @Test
    public void checkResult_LOGARITHM_N() { // ln(e)
        Calculator.setStack();
        assertEquals(new Double(1.0), Calculator.calculate("n(e)"));
    }

    @Test
    public void checkResult_SIN_Rad() { // sin(90)
        Calculator.setStack();
        Calculator.setRad(false);
        assertEquals(new Double(0.8939966636005601), Calculator.calculate("s(90)"));
    }

    @Test
    public void checkResult_SIN_Deg() { // sin(90)
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(1.0), Calculator.calculate("s(90)"));
    }

    @Test
    public void checkResult_COS_Rad() { // cos(90)
        Calculator.setStack();
        Calculator.setRad(false);
        assertEquals(new Double(-0.44807361612916996), Calculator.calculate("c(90)"));
    }

    @Test
    public void checkResult_COS_Deg() { // cos(90)
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(0.0), Calculator.calculate("c(90)"));
    }

    @Test
    public void checkResult_TAN_Rad() { // tan(45)
        Calculator.setStack();
        Calculator.setRad(false);
        assertEquals(new Double(1.61977519054386), Calculator.calculate("t(45)"));
    }

    @Test
    public void checkResult_TAN_Deg() { // tan(45)
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(1.0), Calculator.calculate("t(45)"));
    }

    @Test
    public void checkResult_ABSOLUTE_VALUE() { // Abs(-4)
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(4.0), Calculator.calculate("a(-4)"));
    }

    @Test
    public void checkResult_FACTORIAL() {
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(120.0), Calculator.calculate("5!"));
    }

    @Test
    public void checkResult_POWER() {
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(125.0), Calculator.calculate("5^3"));
    }

    @Test
    public void checkResult_SQUARE_ROOT() {
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(12.0), Calculator.calculate("√(144)"));
    }

    @Test
    public void checkResult_PERCENT() {
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(0.2), Calculator.calculate("20%"));
    }

    @Test
    public void checkResult_PERCENT_2() {
        Calculator.setStack();
        Calculator.setRad(true);
        assertEquals(new Double(60), Calculator.calculate("50+20%"));
    }
}