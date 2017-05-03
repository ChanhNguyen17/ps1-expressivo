package expressivo.element;

import java.util.Map;
import java.util.Set;

import expressivo.Expression;

public class Division implements Expression{
    
    private final Expression up, down;
    
    public Division(Expression up, Expression down){
        this.up = up;
        this.down = down;
    }
    
    @Override 
    public String toString(){
        return "("+this.up + "/" + this.down+")";
    };
    
    @Override
    public Expression differentiate(Variable var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String simplify(Map<String, Double> environment) {
        String simplifyUp = this.up.simplify(environment);
        String simplifyDown = this.down.simplify(environment);
        try{
            double valueUp = Double.parseDouble(simplifyUp);
            double valueDown = Double.parseDouble(simplifyDown);
            return String.valueOf(valueUp / valueDown);
        }catch(NumberFormatException e){}
        return "("+simplifyUp + "/" + simplifyDown+")";
    }

    @Override
    public Double value(Map<String, Double> environment) throws NullPointerException {
        return this.up.value(environment) / this.down.value(environment);
    }

    @Override
    public Set<String> allVariables(Set<String> currentSet) {
        Set<String> setUp = this.up.allVariables(currentSet);
        Set<String> setDown = this.down.allVariables(currentSet);
        setUp.addAll(setDown);
        return setUp;
    }

}
