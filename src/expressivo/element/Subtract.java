package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Subtract implements Expression{
    
    private final Expression left, right;
    
    public Subtract(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    
    @Override 
    public String toString(){
        return "("+this.left + " - " + this.right+")";
    };
    
    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        String simplifyLeft = this.left.simplify(environment);
        String simplifyRight = this.right.simplify(environment);
        try{
            double valueLeft = Double.parseDouble(simplifyLeft);
            double valueRight = Double.parseDouble(simplifyRight);
            return String.valueOf(valueLeft - valueRight);
        }catch(NumberFormatException e){}
        return "("+simplifyLeft + " - " + simplifyRight+")";
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        // TODO Auto-generated method stub
        return null;
    }

}
