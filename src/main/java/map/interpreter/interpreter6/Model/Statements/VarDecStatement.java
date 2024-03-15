package map.interpreter.interpreter6.Model.Statements;


import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.StringType;
import map.interpreter.interpreter6.Model.Values.IValue;

public class VarDecStatement implements IStatement {
    private final String id;
    private final IType type;

    public VarDecStatement(String id, IType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public IStatement deepCopy() {
        return new VarDecStatement(id, type.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        if(symTable.containsKey(this.id).getValue())
            throw new MyException("Variable is already declared");
        symTable.add(id, type.defaultValue());
        return null;
    }
    @Override
    public String toString() {
        return this.type.toString() + " " + this.id+ ";\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        typeEnv.add(this.id, this.type);
        return typeEnv;
    }
}
