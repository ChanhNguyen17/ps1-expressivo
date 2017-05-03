package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Number implements Expression{
    
    public final Double number;
    
    public Number(double number){
        this.number = number;
    }
    
    @Override 
    public String toString(){
        return this.number.toString();
    };
    
    @Override
    public boolean equals(Object thatObject){
        if(thatObject==null || this.getClass()!=thatObject.getClass()){
            return false;
        }
        return this.number.equals(((Number)thatObject).number);
    }
    
    @Override
    public int hashCode(){
        return this.number.hashCode();
    }

    @Override
    public Expression differentiate(Variable var) {
        return new Number(0);
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        return this.number.toString();
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException {
        return this.number;
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        return currentSet;
    };
}
