/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

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

    // Testing strategy
    // differentiate:
    //  Number, Variable, Plus, Multiply:
    //      partition on length of var: 1, >1
    //  Number:
    //      partition on number of this: 0, 1, >1
    //  Variable:
    //      partition on variable of this: equals to var or not
    //  Plus, Multiply:
    //      partition on number of variables of this: 0, 1, >1
    //      partition on left expr: contains var or not
    //      partition on right expr: contains var or not
    //      partition on left or right expr: Number, Variable, Plus, Multiply

    // Covers Number:
    //  length of var: 1, >1
    //  number of this: 0, 1, >1
    @Test
    public void testDifferentiateNumber() {
        Expression num1 = new Number(0);
        Expression num2 = new Number(5.5);
        Expression num3 = new Number(1);

        // 0 的导数应该是 0
        assertEquals(new Number(0), num1.differentiate("xy"));

        // 任意常数的导数应该是 0
        assertEquals(new Number(0), num2.differentiate("x"));
        assertEquals(new Number(0), num3.differentiate("jiahao"));
    }

    // Covers Variable:
    //  length of var: 1, >1
    //  variable of this: equals to var or not
    @Test
    public void testDifferentiateVariable() {
        Expression varX = new Variable("x");
        Expression varY = new Variable("yyy");

        // 变量 x 对 x 求导应该是 1
        assertEquals(new Number(1), varX.differentiate("x"));

        // 变量 y 对 y 求导应该是 1
        assertEquals(new Number(1), varY.differentiate("yyy"));

        // 变量 yyy 对 x 求导应该是 0
        assertEquals(new Number(0), varY.differentiate("x"));


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
        assertEquals(d_plus1, plus1.differentiate("x"));
        assertEquals(d_plus2, plus2.differentiate("x"));
        assertEquals(d_plus3, plus3.differentiate("xyz"));

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
        assertEquals(d_mul1, mul1.differentiate("x"));
        assertEquals(d_mul2, mul2.differentiate("x"));
        assertEquals(d_mul3, mul3.differentiate("xyz"));

        Expression complex1 = new Plus(plus1, mul1);
        Expression complex2 = new Multiply(mul2, plus2);
        Expression d_complex1 = new Plus(d_plus1, d_mul1);
        Expression d_complex2 = new Plus(new Multiply(d_mul2, plus2), new Multiply(mul2, d_plus2));
        assertEquals(d_complex1, complex1.differentiate("x"));
        assertEquals(d_complex2, complex2.differentiate("x"));
    }

    // Testing strategy
    // simplify:
    //  Number, Variable, Plus, Multiply:
    //      partition on length of env: 0, 1, >1
    //      partition on string length of env: 1, >1
    //      partition on double of env: 0, 1, >1
    //  Number:
    //      partition on number of this: 0, 1, >1
    //  Variable:
    //      partition on env and this: env contains variable of this or not
    //  Plus, Multiply:
    //      partition on number of variables of this: 0, 1, >1
    //      partition on env and left expr: env contains variable in left expr or not
    //      partition on env and right expr: env contains variable in right expr or not
    //      partition on left or right expr: Number, Variable, Plus, Multiply

    // Covers Number:
    //      partition on length of env: 0, 1, >1
    //      partition on string length of env: 1, >1
    //      partition on double of env: 0, 1, >1
    //      partition on number of this: 0, 1, >1
    @Test
    public void testSimplifyNumber() {
        Map<String, Double> env0 = Collections.emptyMap();
        Map<String, Double> env1 = Map.of("x", 1.0);
        Map<String, Double> env2 = Map.of("x", 2.0, "yyy", 0.0);

        Expression num1 = new Number(0);
        Expression num2 = new Number(5.5);
        Expression num3 = new Number(1);

        assertEquals(num1, num1.simplify(env0));
        assertEquals(num2, num2.simplify(env1));
        assertEquals(num3, num3.simplify(env2));
    }

    // Covers Variable:
    //      partition on length of env: 0, 1, >1
    //      partition on string length of env: 1, >1
    //      partition on double of env: 0, 1, >1
    //      partition on env and this: env contains variable of this or not
    @Test
    public void testSimplifyVariable() {
        Map<String, Double> env0 = Collections.emptyMap();
        Map<String, Double> env1 = Map.of("x", 1.0);
        Map<String, Double> env2 = Map.of("xxx", 2.0, "yyy", 0.0);

        Expression varX = new Variable("x");
        Expression varY = new Variable("yyy");

        assertEquals(varX, varX.simplify(env0));
        assertEquals(new Number(1), varX.simplify(env1));
        assertEquals(new Number(0), varY.simplify(env2));
    }

    // Covers Plus, Multiply:
    //      partition on length of env: 0, 1, >1
    //      partition on string length of env: 1, >1
    //      partition on double of env: 0, 1, >1
    //      partition on number of variables of this: 0, 1, >1
    //      partition on env and left expr: env contains variable in left expr or not
    //      partition on env and right expr: env contains variable in right expr or not
    //      partition on left or right expr: Number, Variable, Plus, Multiply
    @Test
    public void testSimplifyPlusMultiply() {
        Map<String, Double> env0 = Collections.emptyMap();
        Map<String, Double> env_x = Map.of("x", 1.0);
        Map<String, Double> env_y = Map.of("y", 99.0);
        Map<String, Double> env_xy = Map.of("x", 2.0, "y", 0.0);

        Expression num0 = new Number(0);
        Expression num1 = new Number(1);
        Expression num3 = new Number(3);
        Expression num7 = new Number(7);
        Expression num9 = new Number(9);
        Expression num99 = new Number(99);
        Expression x = new Variable("x");
        Expression y = new Variable("y");

        Expression plus1 = new Plus(x, num3);
        Expression plus2 = new Plus(y, x);
        Expression plus3 = new Plus(num3, num3);

        assertEquals(new Number(4), plus1.simplify(env_x));
        assertEquals(new Number(2), plus2.simplify(env_xy));
        assertEquals(plus2, plus2.simplify(env0));
        assertEquals(new Number(6), plus3.simplify(env_x));

        Expression mul1 = new Multiply(x, num3);
        Expression mul2 = new Multiply(y, x);
        Expression mul3 = new Multiply(num3, num3);
        Expression s_mul2 = new Multiply(num99, x);

        assertEquals(num3, mul1.simplify(env_x));
        assertEquals(s_mul2, mul2.simplify(env_y));
        assertEquals(num9, mul3.simplify(env0));

        // x+3 + x*3
        Expression complex1 = new Plus(plus1, mul1);
        // y*x * (y+x)
        Expression complex2 = new Multiply(mul2, plus2);
        // 99*x * (99+x)
        Expression s_complex2 = new Multiply(new Multiply(num99, x), new Plus(num99, x));

        assertEquals(num7, complex1.simplify(env_x));
        assertEquals(s_complex2, complex2.simplify(env_y));
    }
}
