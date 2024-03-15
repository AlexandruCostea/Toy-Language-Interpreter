package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;

public interface IStatement {
    public IStatement deepCopy();
    PrgState execute(PrgState state) throws MyException;

    IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException;
}
