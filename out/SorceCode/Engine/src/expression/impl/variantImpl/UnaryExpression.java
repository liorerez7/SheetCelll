package expression.impl.variantImpl;

import CoreParts.api.SheetCellViewOnly;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.api.ExpressionVisitor;

public abstract class UnaryExpression implements Expression {

    private Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }
    @Override
    public EffectiveValue evaluate(SheetCellViewOnly sheet) throws IllegalArgumentException {
        return evaluate(expression.evaluate(sheet));
    }
    @Override
    public String getOperationSign() {
        return "";
    }

    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    public String toString() {
        return expression.toString();
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression newExpression) {
        expression = newExpression;
    }
    abstract protected EffectiveValue evaluate(EffectiveValue evaluate);

}
