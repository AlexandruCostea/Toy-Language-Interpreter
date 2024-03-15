package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.IValue;

public class ValueExpression implements IExpression {
    private final IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> table, IHeap<IValue> heap) throws MyException {
        return this.value.deepCopy();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(this.value.deepCopy());
    }

    @Override
    public IType typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        return value.getType();
    }
}
