package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.*;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.ReferenceValue;

import java.util.Map;

public class WriteHeapStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public WriteHeapStatement(String var, IExpression exp) {
        this.varName = var;
        this.expression = exp;
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(this.varName, this.expression.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        if(!symTable.containsKey(this.varName).getValue())
            throw new MyException("Variable doesn't exist");
        IValue val = symTable.getValue(this.varName);
        if(val.getType().equals(new IntType()) || val.getType().equals(new BoolType()) ||
            val.getType().equals(new StringType()))
            throw new MyException("Variable isn't a reference");
        ReferenceValue val1 = (ReferenceValue) val;
        if(!heap.containsAddress(val1.getHeapAddress()).getValue())
            throw new MyException("Variable not found on the heap");
        IValue value = this.expression.eval(symTable, heap);
        if(!value.getType().equals(val1.getLocationType()))
            throw new MyException("Value of expression does not match the value on heap");
        int address = val1.getHeapAddress();
        heap.update(address, value);
        return null;
    }
    @Override
    public String toString() {
        return "wH(" + this.varName + ", " + this.expression.toString() + ");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type1 = typeEnv.getValue(this.varName);
        IType type2 = this.expression.typecheck(typeEnv);
        if (type1.equals(new ReferenceType(type2)))
            return typeEnv;
        else
            throw new MyException("Type Checker Error: right hand side and left hand side have different types ");
    }
}
