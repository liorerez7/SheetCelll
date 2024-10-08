package expression.impl;

import expression.ReturnedValueType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.variantImpl.EffectiveValueImpl;
import expression.impl.variantImpl.TernaryExpression;

public class If extends TernaryExpression {

    public If(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2, EffectiveValue evaluate3) {

        if(evaluate.getCellType() != ReturnedValueType.BOOLEAN && evaluate.getCellType() != ReturnedValueType.UNDEFINED
                && evaluate.getCellType() != ReturnedValueType.UNKNOWN){
            throw new IllegalArgumentException();
        }

        try {
            boolean value = (boolean) evaluate.getValue();
            if(value){
                return evaluate2;
            }
            else{
                return evaluate3;
            }
        }catch (Exception e){
            return new EffectiveValueImpl(ReturnedValueType.BOOLEAN, "UNDIFINED");
        }
    }

    @Override
    public String getOperationSign() {
        return "if";
    }

}
