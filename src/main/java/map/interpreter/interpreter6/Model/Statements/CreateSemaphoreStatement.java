package map.interpreter.interpreter6.Model.Statements;

import javafx.util.Pair;
import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTDictionary;
import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTStack;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.*;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.Expressions.VariableExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.IntType;
import map.interpreter.interpreter6.Model.Types.StringType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateSemaphoreStatement implements IStatement {

    private final String var;

    private final IExpression exp;

    public CreateSemaphoreStatement(String var, IExpression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public IStatement deepCopy() {
        return new CreateSemaphoreStatement(this.var, this.exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        ISem<Integer, Pair<Integer, List<Integer>>>semaphoreTable = state.getSemaphoreTable();

        IValue number1 = new VariableExpression(this.var).eval(symTable, heap);
        if(!number1.getType().equals(new IntType()))
            throw new MyException("Given var is not of type integer");

        int freeAddress = semaphoreTable.getFreeAddress();
        semaphoreTable.add(freeAddress, new Pair<>(((IntValue)number1).getValue(), new ArrayList<>()));
        if (symTable.containsKey(this.var).getValue())
            symTable.update(var, new IntValue(freeAddress));
        else
            throw new MyException("Given variable does not exist");
        return null;
    }

    @Override
    public String toString() {
        return "createSemaphore(" + this.var + "," + this.exp.toString() + ");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException {
        VariableExpression vr = new VariableExpression(this.var);
        IType type1 = vr.typecheck(typeEnv);
        IType type2 = this.exp.typecheck(typeEnv);
        if(!type1.equals(new IntType()) || !type2.equals(new IntType()))
            throw new MyException("TC error: wrong types in create semaphore statement");
        return typeEnv;
    }
}
