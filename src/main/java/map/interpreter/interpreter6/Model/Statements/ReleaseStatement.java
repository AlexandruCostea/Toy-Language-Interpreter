package map.interpreter.interpreter6.Model.Statements;

import javafx.util.Pair;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.ISem;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.VariableExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.IntType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

import java.util.ArrayList;
import java.util.List;

public class ReleaseStatement implements IStatement {

    private final String var;

    public ReleaseStatement(String var) {
        this.var = var;
    }

    @Override
    public IStatement deepCopy() {
        return new ReleaseStatement(this.var);
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        ISem<Integer, Pair<Integer, List<Integer>>> semaphoreTable = state.getSemaphoreTable();

        if(!symTable.containsKey(this.var).getValue())
            throw new MyException("Variable used in acquire statement not found");
        IValue idx = symTable.getValue(this.var);
        int foundIndex = ((IntValue)idx).getValue();
        if(!semaphoreTable.containsKey(foundIndex).getValue())
            throw new MyException("Semaphore index value not found");

        Pair<Integer, List<Integer>> semaphore = semaphoreTable.getValue(foundIndex);

        if(semaphore.getValue().contains(state.getProgramId()))
            for(int i = 0; i < semaphore.getValue().size(); i++) {
                if(semaphore.getValue().get(i) == state.getProgramId()) {
                    semaphore.getValue().remove(i);
                    break;
                }
            }
        return null;
    }

    @Override
    public String toString() {
        return "release(" + this.var + ");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException {
        VariableExpression vr = new VariableExpression(this.var);
        IType type1 = vr.typecheck(typeEnv);
        if(!type1.equals(new IntType()))
            throw new MyException("TC error: wrong types in release statement");
        return typeEnv;
    }
}
