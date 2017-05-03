package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Power implements Expression{
    
    private final Expression base, exponent;
    
    public Power (Expression base, Expression exponent){
        this.base = base;
        this.exponent = exponent;
    }
    
    @Override 
    public String toString(){
        return this.base + " ^ (" + this.exponent+")";
    };
    
    @Override
    public boolean equals(Object thatObject){
        if(thatObject==null || this.getClass()!=thatObject.getClass()){
            return false;
        }
        Power thatObjectPower = (Power)thatObject;
        return this.base.equals(thatObjectPower.base) && this.exponent.equals(thatObjectPower.exponent);
    }
    
    @Override
    public int hashCode(){
        return this.base.hashCode() + (int)(Math.E * this.exponent.hashCode());
    }
    
    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        // TODO Auto-generated method stub
        return null;
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
