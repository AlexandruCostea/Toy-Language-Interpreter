package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.*;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.ReferenceValue;

public class NewStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public NewStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(this.varName, this.expression.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        if(!symTable.containsKey(this.varName).getValue())
            throw new MyException("Variable not declared");
        IType type = symTable.getValue(this.varName).getType();
        if(type.equals(new BoolType()) || type.equals(new IntType()) || type.equals(new StringType()))
            throw new MyException("Invalid data type");
        IType locationType = ((ReferenceValue)symTable.getValue(this.varName)).getLocationType();
        IValue val = this.expression.eval(symTable,heap);
        if(!val.getType().equals(locationType))
            throw new MyException("Variable and expression types don't match");

        int address = heap.add(val);
        ((ReferenceValue)symTable.getValue(this.varName)).setHeapAddress(address);
        return null;
    }
    @Override
    public String toString() {
        return "new(" + this.varName + ", " + this.expression.toString() + ");\n";
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
