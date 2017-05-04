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
        return this.base + "^" + this.exponent;
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
        return (int) Math.pow(this.base.hashCode(), this.exponent.hashCode());
    }
    
    @Override
    public Expression differentiate(Variable var) {
        // Not yet finished
        return this.base.differentiate(var);
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        String simplifyBase = this.base.simplify(environment);
        String simplifyExponent = this.exponent.simplify(environment);
        try{
            double valueBase = Double.parseDouble(simplifyBase);
            double valueExponent = Double.parseDouble(simplifyExponent);
            return String.valueOf(Math.pow(valueBase, valueExponent));
        }catch(NumberFormatException e){}
        return simplifyBase + "^" + simplifyExponent;
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException {
        return Math.pow(this.base.value(environment), this.exponent.value(environment));
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        Set<String> setBase = this.base.allVariables(currentSet);
        Set<String> setExponent = this.exponent.allVariables(currentSet);
        setBase.addAll(setExponent);
        return setBase;
    }
}
