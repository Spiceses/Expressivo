/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    // differentiate:
    //  partition on expression: Number, Variable, Plus, Multiply.
    //  Number, Variable, Plus, Multiply:
    //      partition on length of var: 1, >1
    //  Number:
    //      partition on number of expression: 0, 1, >1
    //  Variable:
    //      partition on variable of expression: equals to var or not
    //  Plus, Multiply:
    //      partition on number of variables of expression: 0, 1, >1
    //      partition on left expr: contains var or not
    //      partition on right expr: contains var or not
    //      partition on left or right expr: Number, Variable, Plus, Multiply
    //      (These tests are very similar to the tests I used for my lower-level differentiation operation)
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // TODO tests for Commands.differentiate() and Commands.simplify()

    // Covers Number:
    //  length of var: 1, >1
    //  number of this: 0, 1, >1
    @Test
    public void testDifferentiateNumber() {
        Expression num1 = new Number(0);
        Expression num2 = new Number(5.5);
        Expression num3 = new Number(1);

        // 0 的导数应该是 0
        assertEquals(new Number(0).toString(), Commands.differentiate(num1.toString(), "xy"));

        // 任意常数的导数应该是 0
        assertEquals(new Number(0).toString(), Commands.differentiate(num2.toString(), "x"));
        assertEquals(new Number(0).toString(), Commands.differentiate(num3.toString(), "jiahao"));
    }

    // Covers Variable:
    //  length of var: 1, >1
    //  variable of this: equals to var or not
    @Test
    public void testDifferentiateVariable() {
        Expression varX = new Variable("x");
        Expression varY = new Variable("yyy");

        // 变量 x 对 x 求导应该是 1
        assertEquals(new Number(1).toString(), Commands.differentiate(varX.toString(), "x"));

        // 变量 y 对 y 求导应该是 1
        assertEquals(new Number(1).toString(), Commands.differentiate(varY.toString(), "yyy"));

        // 变量 yyy 对 x 求导应该是 0
        assertEquals(new Number(0).toString(), Commands.differentiate(varY.toString(), "x"));


    }

    // Covers Plus, Multiply:
    //  length of var: 1, >1
    //  left expr: contains var or not
    //  right expr: contains var or not
    //  number of variables of this: 0, 1, >1
    //  left or right expr: Number, Variable, Plus, Multiply
    @Test
    public void testDifferentiatePlusMultiply() {
        Expression num0 = new Number(0);
        Expression num1 = new Number(1);
        Expression num3 = new Number(3);
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression z = new Variable("z");

        Expression plus1 = new Plus(x, num3);
        Expression plus2 = new Plus(y, x);
        Expression plus3 = new Plus(num3, num3);
        Expression d_plus1 = new Plus(num1, num0);
        Expression d_plus2 = new Plus(num0, num1);
        Expression d_plus3 = new Plus(num0, num0);

        // (x + 3)' = x' + 3' = 1 + 0 = 1
        // d(y + x) / dx = 0 + 1
        // d(3 + 3) / dxyz = 0 + 0
        assertEquals(d_plus1.toString(), Commands.differentiate(plus1.toString(), "x"));
        assertEquals(d_plus2.toString(), Commands.differentiate(plus2.toString(), "x"));
        assertEquals(d_plus3.toString(), Commands.differentiate(plus3.toString(), "xyz"));

        Expression mul1 = new Multiply(x, num3);
        Expression mul2 = new Multiply(y, x);
        Expression mul3 = new Multiply(num3, num3);
        Expression mul30 = new Multiply(num3, num0);
        Expression mul03 = new Multiply(num0, num3);
        Expression mul13 = new Multiply(num1, num3);
        Expression mulx0 = new Multiply(x, num0);
        Expression mul0x = new Multiply(num0, x);
        Expression muly1 = new Multiply(y, num1);
        Expression d_mul1 = new Plus(mul13, mulx0);
        Expression d_mul2 = new Plus(mul0x, muly1);
        Expression d_mul3 = new Plus(mul03, mul30);

        // d(x*3) / dx = 1*3 + x*0
        // d(y*x) / dx = 0*x + y*1
        // d(3*3) / dxyz = 0*3 + 3*0
        assertEquals(d_mul1.toString(), Commands.differentiate(mul1.toString(), "x"));
        assertEquals(d_mul2.toString(), Commands.differentiate(mul2.toString(), "x"));
        assertEquals(d_mul3.toString(), Commands.differentiate(mul3.toString(), "xyz"));

        Expression complex1 = new Plus(plus1, mul1);
        Expression complex2 = new Multiply(mul2, plus2);
        Expression d_complex1 = new Plus(d_plus1, d_mul1);
        Expression d_complex2 = new Plus(new Multiply(d_mul2, plus2), new Multiply(mul2, d_plus2));
        assertEquals(d_complex1.toString(), Commands.differentiate(complex1.toString(), "x"));
        assertEquals(d_complex2.toString(), Commands.differentiate(complex2.toString(), "x"));
    }
}
