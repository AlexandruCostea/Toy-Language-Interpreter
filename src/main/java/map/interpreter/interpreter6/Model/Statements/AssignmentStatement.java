package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.IValue;

public class AssignmentStatement implements IStatement {
    private final String id;
    private final IExpression exp;

    public AssignmentStatement(String id, IExpression exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(this.id, this.exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        if(!symTable.containsKey(this.id).getValue())
            throw new MyException("Can't use undeclared variable");
        IValue val = this.exp.eval(symTable,heap);
        IType idType = symTable.getValue(this.id).getType();
        if(!val.getType().equals(idType))
            throw new MyException("Type of declared variable and expression don't match");
        symTable.update(id, val);
        return null;
    }

    @Override
    public String toString() {
        return this.id + "=" + this.exp.toString() + ";\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type1 = typeEnv.getValue(id);
        IType type2 = this.exp.typecheck(typeEnv);
        if(type1.equals(type2))
            return typeEnv;
        else
            throw new MyException("Type Checker Error: right hand side and left hand side have different types ");
    }
}
