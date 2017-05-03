package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Plus implements Expression{
    private final Expression left, right;
    
    public Plus(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    
    @Override 
    public String toString(){
        return "("+this.left + " + " + this.right+")";
    };
    
    @Override
    public boolean equals(Object thatObject){
        if(thatObject==null || this.getClass()!=thatObject.getClass()){
            return false;
        }
        Plus thatObjectPlus = (Plus)thatObject;
        return this.left.equals(thatObjectPlus.left) && this.right.equals(thatObjectPlus.right);
    }
    
    @Override
    public int hashCode(){
        return this.left.hashCode() + this.right.hashCode();
    }

    @Override
    public Expression differentiate(Variable var) {
        Expression diffLeft = this.left.differentiate(var);
        Expression diffRight = this.right.differentiate(var); 
        return new Plus(diffLeft, diffRight);
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        String simplifyLeft = this.left.simplify(environment);
        String simplifyRight = this.right.simplify(environment);
        try{
            double valueLeft = Double.parseDouble(simplifyLeft);
            double valueRight = Double.parseDouble(simplifyRight);
            return String.valueOf(valueLeft + valueRight);
        }catch(NumberFormatException e){}
        return "("+simplifyLeft + " + " + simplifyRight+")";
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException {
        return this.left.value(environment) + this.right.value(environment);
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        Set<String> setLeft = this.left.allVariables(currentSet);
        Set<String> setRight = this.right.allVariables(currentSet);
        setLeft.addAll(setRight);
        return setLeft;
    };
}
