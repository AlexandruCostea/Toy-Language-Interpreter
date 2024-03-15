package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IStack;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.*;
import map.interpreter.interpreter6.Model.Types.IType;

public class CompoundStatement implements IStatement {

    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(this.first.deepCopy(), this.second.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStack<IStatement> stk = state.getExeStack();
        stk.push(this.second);
        stk.push(this.first);
        return null;
    }

    @Override
    public String toString() {
        return this.first.toString() + this.second.toString();
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        return this.second.typecheck(this.first.typecheck(typeEnv));
    }
}
