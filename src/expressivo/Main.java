package expressivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib6005.parser.UnableToParseException;

/**
 * Console interface to the expression system.
 * 
 * <p>PS1 instructions: you are free to change this user interface class.
 */
public class Main {
    
    
    /**
     * Read expression and command inputs from the console and output results.
     * An empty input terminates the program.
     * @param args unused
     * @throws IOException if there is an error reading the input
     * @throws UnableToParseException 
     */
//    public static void main(String[] args){
//        
//        String aStr = "2*(x+y)+t";
//        String bStr = "2*x+t+2*y";
//        
//        System.out.println(compare(aStr, bStr));
//    }
    
    public static boolean compare(String aStr, String bStr){
        Expression a = Expression.parse(aStr);
        Expression b = Expression.parse(bStr);
        Set<String> aVariables = new HashSet<>();
        Set<String> bVariables = new HashSet<>();
        aVariables = a.allVariables(aVariables);
        bVariables = b.allVariables(bVariables);
        if(!aVariables.equals(bVariables)){
            return false;
        }
        return compareByExpression(a, b, aVariables);
    }
    
    public static boolean compareByExpression(Expression a, Expression b, Set<String> variables){
        Random randomize = new Random();
        int checkTimes = 5;
        double ea = 10e-7;
        
        for(int check = 0; check < checkTimes; check++){
            Map<String,Double> envir = new HashMap<>();
            for(String var : variables){
                double num = randomize.nextGaussian();
                envir.put(var, num);
            }
            try{
                Double valueA = a.value(envir);
                Double valueB = b.value(envir);
                if(Math.abs(valueA-valueB) > ea){
                    return false;
                }
            }catch(NullPointerException e){
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) throws IOException{
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Optional<String> currentExpression = Optional.empty();
        
        while (true) {
            System.out.print("> ");
            final String input = in.readLine();
            
            if (input.isEmpty()) {
                return; // exits the program
            }
            
            try {
                final String output;
                
                if (input.startsWith(DIFFERENTIATE_PREFIX)) {
                    final String variable = parseDifferentiate(input);
                    output = Commands.differentiate(currentExpression.get(), variable);
                    currentExpression = Optional.of(output);
                } else if (input.startsWith(SIMPLIFY_PREFIX)) {
                    final Map<String,Double> environment = parseSimpify(input);
                    output = Commands.simplify(currentExpression.get(), environment);
                    // ... but don't change currentExpression
                } else {
                    final Expression expression = Expression.parse(input);
                    output = expression.toString();
                    currentExpression = Optional.of(output);
                }
                
                System.out.println(output);
            } catch (NoSuchElementException nse) {
                // currentExpression was empty
                System.out.println("must enter an expression before using this command");
            } catch (RuntimeException re) {
                System.out.println(re.getClass().getName() + ": " + re.getMessage());
            }
        }
    }
 
    private static final String DIFFERENTIATE_PREFIX = "!d/d";
    private static final String VARIABLE = "[A-Za-z]+";
    private static final String DIFFERENTIATE = DIFFERENTIATE_PREFIX + "(" + VARIABLE + ") *";

    private static String parseDifferentiate(final String input) {
        final Matcher commandMatcher = Pattern.compile(DIFFERENTIATE).matcher(input);
        if (!commandMatcher.matches()) {
            throw new CommandSyntaxException("usage: !d/d must be followed by a variable name");
        }

        final String variable = commandMatcher.group(1);
        return variable;
    }
    
    private static final String SIMPLIFY_PREFIX = "!simplify";
    private static final String ASSIGNMENT = "(" + VARIABLE + ") *= *([^ ]+)";
    private static final String SIMPLIFY = SIMPLIFY_PREFIX + "( +" + ASSIGNMENT + ")* *";    

    private static Map<String,Double> parseSimpify(final String input) {
        final Matcher commandMatcher = Pattern.compile(SIMPLIFY).matcher(input);
        if (!commandMatcher.matches()) {
            throw new CommandSyntaxException("usage: !simplify var1=val1 var2=val2 ...");
        }
        
        final Map<String,Double> environment = new HashMap<>();
        final Matcher argumentMatcher = Pattern.compile(ASSIGNMENT).matcher(input);
        while (argumentMatcher.find()) {
            final String variable = argumentMatcher.group(1);
            final double value = Double.valueOf(argumentMatcher.group(2));
            environment.put(variable, value);
        }

        // un-comment the following line to print the environment after each !simplify command
        //System.out.println(environment);
        return environment;
    }
    
    public static class CommandSyntaxException extends RuntimeException {
        private static final long serialVersionUID = 1;
        public CommandSyntaxException(String message) {
            super(message);
        }
    }
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
