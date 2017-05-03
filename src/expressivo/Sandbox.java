package expressivo;
import java.util.HashMap;
import java.util.Map;
import expressivo.staff.Polynomial;
import expressivo.staff.Parser;
import lib6005.parser.*;

public class Sandbox {
    enum IntegerGrammar {ROOT, SUM, PRIMITIVE, NUMBER, WHITESPACE};
    
    static String exprS;
    static Polynomial polyS;
    static Polynomial d_dxS;
    static Polynomial d_dyS;
    static String skipS;
    
    public static void main(String[] args){
        System.out.println((int)(Math.E*100));
    }
    
    public static void ExpressivoTest(String name, String expr, String skip) {
        exprS = expr;
        polyS = Parser.parse(expr);
        d_dxS = polyS.differentiate("x");
        d_dyS = polyS.differentiate("y");
        skipS = skip;
    }
    
    public static boolean testSimplify_xyz() {
        if (skipS.contains("simplify")) { return false; /* manual skip */ }
        
        final String student;
        final Polynomial result;
              
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 2.0);
        environment.put("y", 3.0);
        environment.put("z", 4.0);
        try {
            student = Commands.simplify(exprS, environment);
        } catch (Exception e) {
            throw new RuntimeException("error calling simplify()", e);
        }
        try {
            result = Parser.parse(student);
        } catch (Exception e) {
            throw new RuntimeException("invalid simplified expression (staff parser could not parse)", e);
        }
        Polynomial evaluatedPoly = polyS.evaluate(environment);
        if(!polyS.evaluate(environment).round(4).equals(result.round(4))){
            return false;
        }
//        assertEquals("(after converting to canonical form)", polyS.evaluate(environment).round(4), result.round(4));
        if (evaluatedPoly.isConstant()){
            if(!student.replaceAll("[ ()]", "").matches("(\\d*\\.)?\\d+")){
                return false;
            }
//            assertTrue("Expected simplified expression " + student + " to be a constant.", 
//                    student.replaceAll("[ ()]", "").matches("(\\d*\\.)?\\d+"));
        }
        return true;
    }
    
    public static boolean testParse() {
        if (skipS.contains("parse")) { return false; /* manual skip */ }
        
        final Expression student;
        final Polynomial result;
        try {
            student = Expression.parse(exprS);
            System.out.println("here");
        } catch (Exception e) {
            throw new RuntimeException("error calling parse()", e);
        }
        try {
            result = Parser.parse(student.toString());
            System.out.println("no here");
        } catch (Exception e) {
            throw new RuntimeException("invalid Expression.toString (staff parser could not parse)", e);
        }
        return polyS.round(4).equals(result.round(4));
//        assertEquals("(after converting to canonical form)", polyS.round(4), result.round(4));
    }
    
    /**
     * Traverse a parse tree, indenting to make it easier to read.
     * @param node
     * Parse tree to print.
     * @param indent
     * Indentation to use.
     */
    static void visitAll(ParseTree<IntegerGrammar> node, String indent){
        if(node.isTerminal()){
            System.out.println(indent + node.getName() + ":" + node.getContents());
        }else{
            System.out.println(indent + node.getName());
            for(ParseTree<IntegerGrammar> child: node){
                visitAll(child, indent + "   ");
            }
        }
    }
}
