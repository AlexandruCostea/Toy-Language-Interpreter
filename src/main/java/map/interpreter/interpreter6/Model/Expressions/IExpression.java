package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.IValue;

public interface IExpression {
    IExpression deepCopy();
    IValue eval(IDictionary<String, IValue> table, IHeap<IValue> heap) throws MyException;

    IType typecheck(IDictionary<String,IType> typeEnv) throws MyException;
}
