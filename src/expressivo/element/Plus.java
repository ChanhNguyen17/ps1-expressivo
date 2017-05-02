package expressivo.element;

import java.util.Map;

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
    };
}
