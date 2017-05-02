package expressivo;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib6005.parser.*;

public class Sandbox {
    enum IntegerGrammar {ROOT, SUM, PRIMITIVE, NUMBER, WHITESPACE};
    public static void main(String[] args) throws UnableToParseException, IOException{
        Parser<IntegerGrammar> parser =
                GrammarCompiler.compile(new File("IntegerExpression.g"), IntegerGrammar.ROOT);
        ParseTree<IntegerGrammar> tree = parser.parse("5+((02 +3)+21)");
        System.out.println(tree.toString());
        System.out.println(tree.childrenByName(IntegerGrammar.SUM));
        tree.display();
        visitAll(tree,">");
//        IntegerExpression exp = buildAST(tree);
//        System.out.println(exp.eval());
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
