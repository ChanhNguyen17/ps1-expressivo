package expressivo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import expressivo.element.Multiply;
import expressivo.element.Number;
import expressivo.element.Plus;
import expressivo.element.Variable;
import lib6005.parser.*;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //   TODO
    enum IntegerGrammar {ROOT, SUM, PRODUCT, PRIMITIVE, NUMBER, VARIABLE, WHITESPACE};
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IOException 
     * @throws UnableToParseException 
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input){
        Parser<IntegerGrammar> parser = null;
        ParseTree<IntegerGrammar> tree = null;
        try {
            parser = GrammarCompiler.compile(new File("src/expressivo/Expression.g"), IntegerGrammar.ROOT);
            tree = parser.parse(input);
        } catch (UnableToParseException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        tree.display();
        return Expression.buildAST(tree);
    }
    
    /**
     * Function converts a ParseTree to an IntegerExpression. 
     * @param p
     *  ParseTree<IntegerGrammar> that is assumed to have been constructed by the grammar in IntegerExpression.g
     * @return
     */
    static Expression buildAST(ParseTree<IntegerGrammar> p){
        switch(p.getName()){
        /*
         * Since p is a ParseTree parameterized by the type IntegerGrammar, p.getName() 
         * returns an instance of the IntegerGrammar enum. This allows the compiler to check
         * that we have covered all the cases.
         */
        case NUMBER:
            /*
             * A number will be a terminal containing a number.
             */
            return new Number(Double.parseDouble(p.getContents()));
        case VARIABLE:
            /*
             * A variable will be a terminal containing a sign.
             */
            return new Variable(p.getContents());     
        case PRIMITIVE:
            /*
             * A primitive will have either a number or a sum as child (in addition to some whitespace)
             * By checking which one, we can determine which case we are in.
             */                         
            if(!p.childrenByName(IntegerGrammar.NUMBER).isEmpty()){
                return buildAST(p.childrenByName(IntegerGrammar.NUMBER).get(0)); 
            }else if(!p.childrenByName(IntegerGrammar.VARIABLE).isEmpty()){
                return buildAST(p.childrenByName(IntegerGrammar.VARIABLE).get(0));
            }else if(!p.childrenByName(IntegerGrammar.SUM).isEmpty()){
                return buildAST(p.childrenByName(IntegerGrammar.SUM).get(0));
            }
//            else{
//                return buildAST(p.childrenByName(IntegerGrammar.PRODUCT).get(0));
//            }
        case SUM:
            /*
             * A sum will have one or more children that need to be summed together.
             * Note that we only care about the children that are primitive. There may also be 
             * some whitespace children which we want to ignore.
             */
            boolean first = true;
            Expression result = null;
            for(ParseTree<IntegerGrammar> child : p.childrenByName(IntegerGrammar.PRODUCT)){                
                if(first){
                    result = buildAST(child);
                    first = false;
                }else{
                    Expression zero = new Number(0);
                    Expression resultRight = buildAST(child);
                    if(zero.equals(result)){
                        result = resultRight;
                    }else if(!zero.equals(resultRight)){
                        result = new Plus(result, resultRight);
                    }
                }
            }
            if (first) {
                throw new RuntimeException("sum must have a non whitespace child:" + p);
            }
            return result;
        case PRODUCT:
            boolean firstM = true;
            Expression resultM = null;
            for(ParseTree<IntegerGrammar> child : p.childrenByName(IntegerGrammar.PRIMITIVE)){                
                if(firstM){
                    resultM = buildAST(child);
                    firstM = false;
                }else{
                    Expression zero = new Number(0);
                    if(zero.equals(resultM)){
                        return zero;}
                    
                    Expression one = new Number(1);
                    Expression resultMRight = buildAST(child);
                    if(zero.equals(resultMRight)){
                        return zero;}
                    
                    if(one.equals(resultM)){
                        resultM = resultMRight;
                    }else if(!one.equals(resultMRight)){
                        resultM = new Multiply(resultM, resultMRight);
                    }
                }
            }
            if (firstM) {
                throw new RuntimeException("product must have a non whitespace child:" + p);
            }
            return resultM;    
        case ROOT:
            /*
             * The root has a single sum child, in addition to having potentially some whitespace.
             */
            return buildAST(p.childrenByName(IntegerGrammar.SUM).get(0));
        case WHITESPACE:
            /*
             * Since we are always avoiding calling buildAST with whitespace, 
             * the code should never make it here. 
             */
            throw new RuntimeException("You should never reach here:" + p);
        }   
        /*
         * The compiler should be smart enough to tell that this code is unreachable, but it isn't.
         */
        throw new RuntimeException("You should never reach here:" + p);
    }
    
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
    
    public Expression differentiate(Variable var);
    public String simplify(Map<String,Double> environment);
}
