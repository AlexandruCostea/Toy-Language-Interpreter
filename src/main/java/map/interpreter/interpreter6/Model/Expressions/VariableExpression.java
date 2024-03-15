package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.IValue;

public class VariableExpression implements IExpression {
    private final String id;

    public VariableExpression(String id) {
        this.id = id;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> table, IHeap<IValue> heap) throws MyException {
        return table.getValue(id).deepCopy();
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(this.id);
    }

    @Override
    public IType typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        return typeEnv.getValue(this.id);
    }
}
