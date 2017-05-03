package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Variable implements Expression{
    
    public final String sign;
    
    public Variable(String sign){
        this.sign = sign;
    }
    
    @Override 
    public String toString(){
        return this.sign;
    };
    
    @Override
    public boolean equals(Object thatObject){
        if(thatObject==null || this.getClass()!=thatObject.getClass()){
            return false;
        }
        return this.sign.equals(((Variable)thatObject).sign);
    }
    
    @Override
    public int hashCode(){
        return this.sign.hashCode();
    }

    @Override
    public Expression differentiate(Variable var) {
        if(this.equals(var)){
            return new Number(1);
        }
        return new Number(0);
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        Double value = environment.get(this.sign);
        return (value==null)? this.toString() : value.toString();
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException {
        return environment.get(sign);
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        currentSet.add(this.sign);
        return currentSet;
    }
}
