/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    // Number:
    //  toString, hashCode, equals:
    //      partition on number: 0, >0
    //  equals:
    //      partition on type of obj: class of this, others
    //      partition on number of obj: 0, >0
    // Variable:
    //  toString, hashCode, equals:
    //      partition on length of variable: 1, >1
    //  equals:
    //      partition on type of obj: class of this, others
    //      partition on length of variable in obj: 1, >1
    // Plus, Multiply:
    //  toString, hashCode, equals:
    //      partition on type of left or right expr: Number, Variable, Plus, Multiply
    //  equals:
    //      partition on type of obj: class of this, others
    //      partition on type of left or right expr in obj: Number, Variable, Plus, Multiply
    //      partition on number of equivalent expressions: 0, 1(left = left or right = right, left = right) , 2(left = left and right = right)
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Covers Number:
    //  toString, hashCode, equals:
    //      number: 0, >0
    //  equals:
    //      type of obj: class of this, others
    @Test
    public void testNumber() {
        Expression num1 = new Number(0);
        Expression num2 = new Number(5.5);
        Expression num3 = new Number(5.5);

        // toString 测试
        assertEquals("0.0", num1.toString());
        assertEquals("5.5", num2.toString());

        // equals 测试
        assertNotEquals(num1, num2);
        assertEquals(num2, num3);
        assertNotEquals(num1, new Variable("x"));

        // hashCode 测试
        assertEquals(num2.hashCode(), num3.hashCode());
        assertNotEquals(num1.hashCode(), num3.hashCode());
    }

    // Covers Variable:
    //  toString, hashCode, equals:
    //      length of variable: 1, >1
    //  equals:
    //      type of obj: class of this, others
    //      length of variable in obj: 1, >1
    @Test
    public void testVariable() {
        Expression var1 = new Variable("x");
        Expression var2 = new Variable("xyz");
        Expression var3 = new Variable("x");

        // toString 测试
        assertEquals("x", var1.toString());
        assertEquals("xyz", var2.toString());

        // equals 测试
        assertNotEquals(var1, var2);
        assertEquals(var1, var3);
        assertNotEquals(var1, new Plus(var2, var3));

        // hashCode 测试
        assertEquals(var1.hashCode(), var3.hashCode());
        assertNotEquals(var2.hashCode(), var3.hashCode());
    }

    // Covers Plus:
    //  toString, hashCode, equals:
    //      type of left or right expr: Number, Variable, Plus, Multiply
    //  equals:
    //      type of obj: class of this, others
    //      type of left or right expr in obj: Number, Variable, Plus, Multiply
    //      number of equivalent expressions: 0, 1(left = left or right = right, left = right) , 2(left = left and right = right)
    @Test
    public void testPlus() {
        Expression number = new Number(2);
        Expression var = new Variable("x");
        Expression expr1 = new Plus(number, var);
        Expression expr2 = new Plus(number, var);
        Expression expr3 = new Plus(new Plus(number, var), new Multiply(number, var));

        // toString 测试
        assertEquals("(2.0 + x)", expr1.toString());
        assertEquals("((2.0 + x) + (2.0 * x))", expr3.toString());

        // equals 测试
        assertEquals(expr1, expr2);
        assertNotEquals(expr1, var);
        assertNotEquals(expr2, expr3);
        assertNotEquals(expr1, new Plus(number, number));
        assertNotEquals(expr1, new Plus(var, var));

        // hashCode 测试
        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }

    // Covers Multiply:
    //  toString, hashCode, equals:
    //      type of left or right expr: Number, Variable, Plus, Multiply
    //  equals:
    //      type of obj: class of this, others
    //      type of left or right expr in obj: Number, Variable, Plus, Multiply
    //      number of equivalent expressions: 0, 1(left = left or right = right, left = right) , 2(left = left and right = right)
    @Test
    public void testMultiply() {
        Expression number = new Number(3);
        Expression var = new Variable("y");
        Expression expr1 = new Multiply(number, var);
        Expression expr2 = new Multiply(number, var);
        Expression expr3 = new Multiply(new Multiply(number, var), new Plus(number, var));

        // toString 测试
        assertEquals("(3.0 * y)", expr1.toString());
        assertEquals("((3.0 * y) * (3.0 + y))", expr3.toString());

        // equals 测试
        assertEquals(expr1, expr2);
        assertNotEquals(expr1, var);
        assertNotEquals(expr2, expr3);
        assertNotEquals(expr1, new Multiply(number, number));
        assertNotEquals(expr1, new Multiply(var, var));

        // hashCode 测试
        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }

    // Testing strategy
    // parse:
    //  partition on operators: +, *, ()
    //  partition on num of numbers: 0, 1, >1
    //  partition on num of variables: 0, 1, >1
    //  partition on number: 0, >0
    //  partition on type of number: integer, decimal
    //  partition on length of variable: 1, >1
    //      (below is error handling)
    //  partition on position of space: between items, within terminal


    // Covers parse:
    //  num of numbers: 1
    //  num of variables: 0
    //  number: 0, >0
    //  type of number: integer, decimal
    //  operators: ()
    @Test
    public void testParseNumber() {
        assertEquals(new Number(0), Expression.parse("0"));
        assertEquals(new Number(3.14), Expression.parse("3.14"));
    }

    // Covers parse:
    //  num of numbers: 0
    //  num of variables: 1
    //  length of variable: 1, >1
    @Test
    public void testParseVariable() {
        assertEquals(new Variable("x"), Expression.parse("(x)"));
        assertEquals(new Variable("y"), Expression.parse("y"));
        assertEquals(new Variable("xyz"), Expression.parse("xyz"));
    }

    // Covers parser:
    //  num of numbers: >1
    //  num of variables: >1
    //  operators: +, *
    @Test
    public void testParseMixedExpressions() {
        assertEquals(
                new Plus(new Variable("x"), new Multiply(new Variable("y"), new Variable("z"))),
                Expression.parse("x + y * z")
        );

        assertEquals(
                new Multiply(new Plus(new Variable("x"), new Variable("y")), new Variable("z")),
                Expression.parse("(x + y) * z")
        );

        assertEquals(
                new Multiply(new Plus(new Number(3), new Number(4)), new Plus(new Variable("x"), new Variable("y"))),
                Expression.parse("(3 + 4) * (x + y)")
        );
    }
}
