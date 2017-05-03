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
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // TODO tests for Commands.differentiate() and Commands.simplify()
    
    Number constant = new Number(28.08);
    Variable phi = new Variable("phi");
    Variable psi = new Variable("psi");
    @Test
    public void testSimple(){
        assertEquals(Commands.differentiate(constant.toString(),"phi"),"0.0");
        assertEquals(Commands.differentiate(psi.toString(),"phi"),"0.0");
        assertEquals(Commands.differentiate(phi.toString(),"phi"),"1.0");
    }
    
    Plus p1 = new Plus(constant,phi);
    Plus p2 = new Plus(psi,constant);
    Multiply m1 = new Multiply(constant,phi);
    Multiply m2 = new Multiply(psi,constant);
    Plus twoPhi = new Plus(phi,phi);
    Multiply phiSq = new Multiply(phi,phi);
    Plus powPlus = new Plus(new Multiply(phi,phiSq),phi); 
    Multiply prodMult = new Multiply(new Plus(phi,twoPhi),phiSq);
    
    
    //Same testing strategy as for Expression but with strings as output
    //Also ensure that single letters, capital, words all work
    @Test
    public void testCompound(){
        assertEquals(Commands.differentiate((new Variable("x")).toString(),"x"),"1.0");
        assertEquals(Commands.differentiate((new Variable("x")).toString(),"X"),"0.0");
        assertEquals(Commands.differentiate((new Variable("xmas")).toString(),"xmas"),"1.0");
        assertEquals(Commands.differentiate(p1.toString(),"phi"),"1.0");
        assertEquals(Commands.differentiate(p2.toString(),"phi"),"0.0");
        assertEquals(Commands.differentiate(m1.toString(),"phi"),"28.08");
        assertEquals(Commands.differentiate(m2.toString(),"phi"),"0.0");
        assertEquals(Commands.differentiate(powPlus.toString(),"phi"),"(((phi * (phi + phi)) + (phi * phi))"
                + " + 1.0)");
        assertEquals(Commands.differentiate(prodMult.toString(),"phi"), "(((phi + (phi + phi)) * (phi + phi))"+
                                                                            " + ((1.0 + (1.0 + 1.0)) * (phi * phi)))");
    }
    
    //Same testing strategy as for Expression but in addition
    //ensuring that it works for one and more than one var=val mappings for a given expression
    //Also test the regex parsing of the (var=val (var=val)*)* input for floats and scientific notation
    //Also test simplify without mapping
    //Also test single letter, word, caps
    @Test
    public void testSimplify(){
        Plus test1 = new Plus(new Number(28),new Plus(new Number(0.08),phi));
        Multiply test2 = new Multiply(new Plus(constant,new Number(7)),test1);
        Variable x = new Variable("x");
        Map<String, Double> envir = new HashMap<>();
        
        envir.put("x", 16.47e-9);
        assertEquals(Commands.simplify(x.toString(), envir),"1.647E-8");
        
        envir.put("x", 16.47E+9);
        assertEquals(Commands.simplify(x.toString(), envir),"1.647E10");
        
        envir.put("x", 1.4E19);
        assertEquals(Commands.simplify(x.toString(), envir),"1.4E19");
        
        envir.put("x", 16.47e-9);
        assertEquals(Commands.simplify(x.toString(), envir),"x");
        
        envir.put(test1.toString(), 0.0);
        assertEquals(Commands.simplify(test1.toString(),envir),"(28.08 + phi)");
        
        envir.put(test2.toString(), 0.0);
        assertEquals(Commands.simplify(test2.toString(),envir),"(35.08 * (28.08 + phi))");
        
        envir.put("phi", 5.0);
//        assertEquals(Commands.simplify(constant.toString(),vars),"28.08");
//        assertEquals(Commands.simplify(phi.toString(),vars),"5.0");
//        assertEquals(Commands.simplify(psi.toString(),vars),"psi");
//        assertEquals(Commands.simplify(p1.toString(),vars),"33.08");
//        Expression p2rev = Expression.sum(constant.toString(),psi);
//        assertEquals(Commands.simplify(p2.toString(),vars),"(28.08 + psi)");
//        assertEquals(Commands.simplify(phiSq.toString(),vars),"25.0");
//        assertEquals(Commands.simplify((new Multiply(p2,phiSq),vars)).toString(),"(25.0 * (28.08 + psi))");
//        Double n = (28.08*28.08*5);
//        vars = "phi=5.0 psi=6";
//        assertEquals(Commands.simplify(p1.toString(),vars),"33.08");
    }
    
}
