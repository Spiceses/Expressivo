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
    //      partition on length of variable: 0, 1, >1
    //  equals:
    //      partition on type of obj: class of this, others
    //      partition on length of variable in obj: 0, 1, >1
    // Plus, Multiply:
    //  toString, hashCode, equals:
    //      partition on type of left or right expr: Number, Variable, Plus, Multiply
    //  equals:
    //      partition on type of obj: class of this, others
    //      partition on type of left or right expr in obj: Number, Variable, Plus, Multiply

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // TODO tests for Expression
    
}
