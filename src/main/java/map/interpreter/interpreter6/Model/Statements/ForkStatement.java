package map.interpreter.interpreter6.Model.Statements;

import javafx.util.Pair;
import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTDictionary;
import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTHeap;
import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTStack;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.*;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

public class ForkStatement implements IStatement {
    private final IStatement threadStatement;

    public ForkStatement(IStatement statement) {
        this.threadStatement = statement;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(this.threadStatement.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStack<IStatement> newStack = new ADTStack<>();
        IDictionary<String, IValue> newSymTable = new ADTDictionary<>();
        for (Map.Entry<String, IValue> entry : state.getSymTable().getDictionary().entrySet()) {
            newSymTable.add(entry.getKey(), entry.getValue().deepCopy());
        }
        IHeap<IValue> newHeap = state.getHeap();
        IDictionary<StringValue, BufferedReader> newFileTable = state.getFileTable();
        IList<IValue> newOut = state.getOut();
        ISem<Integer, Pair<Integer, List<Integer>>> newSemTbl = state.getSemaphoreTable();
        return new PrgState(newStack, newSymTable, newHeap, newFileTable, newOut, this.threadStatement.deepCopy(), newSemTbl);
    }
    @Override
    public String toString() {
        return "fork(" + this.threadStatement.toString() + ");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IDictionary<String, IType> typeEnvClone = new ADTDictionary<>();
        for(Map.Entry<String, IType> entry: typeEnv.getDictionary().entrySet()) {
            typeEnvClone.add(entry.getKey(), entry.getValue().deepCopy());
        }
        this.threadStatement.typecheck(typeEnvClone);
        return typeEnv;
    }
}
