package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Multiply implements Expression{
    private final Expression left, right;
    
    public Multiply(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    
    @Override 
    public String toString(){
        return "("+this.left + "*" + this.right+")";
    };
    
    @Override
    public boolean equals(Object thatObject){
        if(thatObject==null || this.getClass()!=thatObject.getClass()){
            return false;
        }
        Multiply thatObjectMultiplication = (Multiply)thatObject;
        return this.left.equals(thatObjectMultiplication.left) && this.right.equals(thatObjectMultiplication.right);
    }
    
    @Override
    public int hashCode(){
        return this.left.hashCode() * this.right.hashCode();
    }

    @Override
    public Expression differentiate(Variable var) {
        Expression diffLeft = new Multiply(this.left, this.right.differentiate(var));
        Expression diffRight = new Multiply(this.right, this.left.differentiate(var));
        return new Plus(diffLeft, diffRight);
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        String simplifyLeft = this.left.simplify(environment);
        String simplifyRight = this.right.simplify(environment);
        try{
            double valueLeft = Double.parseDouble(simplifyLeft);
            double valueRight = Double.parseDouble(simplifyRight);
            return String.valueOf(valueLeft * valueRight);
        }catch(NumberFormatException e){}
        return "("+simplifyLeft + "*" + simplifyRight+")";
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException{
        return this.left.value(environment) * this.right.value(environment);
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        Set<String> setLeft = this.left.allVariables(currentSet);
        Set<String> setRight = this.right.allVariables(currentSet);
        setLeft.addAll(setRight);
        return setLeft;
    };
}
