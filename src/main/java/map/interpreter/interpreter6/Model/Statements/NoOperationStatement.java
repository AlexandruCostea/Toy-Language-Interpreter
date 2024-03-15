package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.ReferenceType;

public class NoOperationStatement implements IStatement {

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        return null;
    }

    @Override
    public String toString() {
        return "No operation;\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        return typeEnv;
    }
}
