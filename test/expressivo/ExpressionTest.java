/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import expressivo.element.Multiply;
import expressivo.element.Number;
import expressivo.element.Plus;
import expressivo.element.Variable;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    /**
     * Testing strategy for parse
     * 
     * Each of the possible operations: sum, product
     * Each non-compound expression: number, variable
     * Whitespace and redundant parentheses should yield the same expression (as determined by equals)
     * Parentheses respected where not redundant
     * All types of whitespace
     * Combinations of compound, compound and simple, simple and simple
     * Variables composed of 1 and more than 1 letters, upper and lowercase
     * Integers and decimal numbers
     * Floating point with no number before, with no number after
     * Whitespace before and after compound and primitive
     * Preservation of order
     * 
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    Number testInt = new Number(12);
    Number testDec = new Number(0.16);
    Number testGreaterThanOneDec = new Number(12.16);
    Variable testLower = new Variable("a");
    Variable testUpper = new Variable("A");
    Variable testWord = new Variable("alPHa");
    Plus testPlusSimple = new Plus(testInt,testLower);
    Plus testPlusSimpleRev = new Plus(testLower,testInt);
    Multiply testMultSimple = new Multiply(testInt,testLower);
    Multiply testMultSimpleRev = new Multiply(testLower,testInt);
    
    @Test 
    public void testParseNumber(){
        assertEquals(Expression.parse("12"),testInt);
        assertEquals(Expression.parse("12."),testInt); 
        assertEquals(Expression.parse("12.0   "),testInt);
        assertEquals(Expression.parse("1.2E+1  "),testInt);
        assertEquals(Expression.parse("12.16"),testGreaterThanOneDec);
        assertEquals(Expression.parse("0.1216E2"),testGreaterThanOneDec);
        assertEquals(Expression.parse("0.16"),testDec);
        assertEquals(Expression.parse("16e-2"),testDec);
        assertEquals(Expression.parse("1600.00e-4"),testDec);
        assertEquals(Expression.parse("\r.16"),testDec);
        assertEquals(Expression.parse("e"),new Variable("e"));
        assertEquals(Expression.parse("E"),new Variable("E"));
        assertEquals(Expression.parse("e+1.9"),new Plus(new Variable("e"),new Number(1.9)));
        assertEquals(Expression.parse("1.9+E"),new Plus(new Number(1.9),new Variable("E")));
        assertEquals(Expression.parse("e*9.8"),new Multiply(new Variable("e"),new Number(9.8)));
        assertEquals(Expression.parse("9.8*E"),new Multiply(new Number(9.8),new Variable("E")));
        
    }
    
    @Test 
    public void testParseVar(){
        assertEquals(Expression.parse("A"),testUpper);
        assertEquals(Expression.parse("\ta"),testLower);
        assertEquals(Expression.parse("alPHa\n"),testWord);
    }
    
    @Test 
    public void testParseSum(){
        assertEquals(Expression.parse("12 + a"),testPlusSimple);
        assertEquals(Expression.parse("  (12 + a)  "),testPlusSimple);
        assertEquals(Expression.parse("a + 12"),testPlusSimpleRev); // already tested case Plus(a,b).equals(Plus(b,a)),
        //for !(b.equals(a)) --- i.e. should be false --- likewise for Multiply
    }
    
    
    @Test 
    public void testParseMult(){
        assertEquals(Expression.parse("12 * a"),testMultSimple);
        assertEquals(Expression.parse("  (12 * a)  "),testMultSimple);
        assertEquals(Expression.parse("a * 12"),testMultSimpleRev);
    }
    
    
    Plus testPlusSimple2 = new Plus(testLower,testInt);
    Multiply testMultSimple2 = new Multiply(testLower,testInt);
    Plus testPlusComplex = new Plus(testPlusSimple2,testMultSimple2);
    Multiply testMultComplex = new Multiply(testMultSimple2,testPlusSimple2);
    Multiply multParens = new Multiply(new Multiply(testWord,testDec),new Plus(testUpper,testGreaterThanOneDec));
    Plus plusParens = new Plus(new Multiply(new Multiply(testWord,testDec),testUpper),testGreaterThanOneDec);
    
    @Test
    public void testGrouping(){

        assertEquals(Expression.parse("(a + 12) + (a*12)"),testPlusComplex);
        //assertEquals(Expression.parse("a + (12 + a*12)"),testPlusComplex);
        assertEquals(Expression.parse("(a * 12) * (a + 12)"),testMultComplex);
        //assertEquals(Expression.parse("a * 12 * (a + 12)"),testMultComplex);
        //assertEquals(Expression.parse("a * (12 * (a + 12))"),testMultComplex);
        //The two expressions below are not necessarily the same and should yield different objects 
        assertEquals(Expression.parse("(alPHa * 0.16) * (A + 12.16)"),multParens);
        assertEquals(Expression.parse("((alPHa * 0.16) * A) + 12.16"),plusParens);
    }
    
    
    //Both sides, 0,1, all types of expressions
    @Test
    public void testSimplification(){
        assertEquals(Expression.parse("9*0"),new Number(0));
        assertEquals(Expression.parse("a*1"),new Variable("a"));
        assertEquals(Expression.parse("1*(a+12)"),testPlusSimple2);
        assertEquals(Expression.parse("((a+12)*0 + (a*12))"),testMultSimple2);
    }
    
    /**
     * Testing strategy for differentiate
     * 
     * Constant - number, other variable
     * Given variable
     * Sum
     * Product
     * Powers
     * Compound sum
     * Compound product
     * Each of constant, variable, sum and product should appear on left and right
     * Equivalent expressions should produce the same results
     * Test for case
     */
    Variable x = new Variable("phi");
    Number constant = new Number(28.08);
    Variable phi = new Variable("phi");
    Variable psi = new Variable("psi");
    Number zero = new Number(0);
    Number one = new Number(1);
    
    @Test
    public void testSimple(){
        assertEquals(constant.differentiate(x),zero);
        assertEquals(psi.differentiate(x),zero);
        assertEquals(phi.differentiate(x),one);
    }
    
    Plus p1 = new Plus(constant,phi);
    Plus p2 = new Plus(psi,constant);
    Multiply m1 = new Multiply(constant,phi);
    Multiply m2 = new Multiply(psi,constant);
    Plus twoPhi = new Plus(phi,phi);
    Multiply phiSq = new Multiply(phi,phi);
    Plus powPlus = new Plus(new Multiply(phi,phiSq),phi); 
    Multiply prodMult = new Multiply(new Plus(phi,twoPhi),phiSq);
    
    @Test
    public void testCompound(){
        assertEquals(p1.differentiate(x),one);
        assertEquals(p2.differentiate(x),zero);
        assertEquals(m1.differentiate(x),constant);
        assertEquals(m2.differentiate(x),zero);
        assertEquals(powPlus.differentiate(x), Expression.parse("(phi*(phi + phi) + (phi*phi)) + 1"));
        assertEquals(prodMult.differentiate(x), Expression.parse("(phi+(phi+phi))*(phi+phi) + (1 + (1 + 1))*(phi*phi)"));
    }
    
    //Testing strategy
    //Compound, simple
    //Substitute one and more than one
    //No variables remain
    //Nothing substituted
    //Substitute on left, right
    //Number term should come first within each expression
    
    @Test
    public void testSimplify(){
        Map<String,Double> values= new HashMap<>();
        values.put(phi.toString(), 5.0);
        assertEquals(constant.simplify(values),constant);
        assertEquals(phi.simplify(values),new Number(5));
        assertEquals(psi.simplify(values),psi);
        assertEquals(p1.simplify(values),new Number(28.08+5));
        assertEquals(phiSq.simplify(values),new Number(25));
        values.put(psi.toString(), 6.0);
        assertEquals(p1.simplify(values),new Number(28.08+5));
    }
    
}
