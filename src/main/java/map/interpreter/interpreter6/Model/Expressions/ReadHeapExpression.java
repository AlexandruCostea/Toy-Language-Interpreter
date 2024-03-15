package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.*;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.ReferenceValue;

public class ReadHeapExpression implements IExpression {
    private final IExpression expression;

    public ReadHeapExpression(IExpression exp) {
        this.expression = exp;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap<IValue> heap) throws MyException {
        IValue val = this.expression.eval(symTable, heap);
        if(val.getType().equals(new IntType()) || val.getType().equals(new BoolType()) ||
        val.getType().equals(new StringType()))
            throw new MyException("Expression does not evaluate to a reference type");
        ReferenceValue val1 = (ReferenceValue) val;
        int address = val1.getHeapAddress();
        if(!heap.containsAddress(address).getValue())
            throw new MyException("Reference read at invalid address");
        return heap.getValue(address).deepCopy();
    }

    @Override
    public String toString() {
        return "rH(" + this.expression.toString() + ")";
    }
    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(this.expression.deepCopy());
    }

    @Override
    public IType typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type=expression.typecheck(typeEnv);
        if (!(type instanceof IntType || type instanceof BoolType || type instanceof StringType)) {
            ReferenceType refType =(ReferenceType) type;
            return refType.getInner();
        } else
            throw new MyException("Type Checker Error: the rH argument is not a Ref Type");
    }
}
