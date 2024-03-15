package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IStack;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.BoolType;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;

import java.util.Map;

public class WhileStatement implements IStatement {
    private final IExpression whileExpression;
    private final IStatement statement;

    public WhileStatement(IExpression exp, IStatement stmt) {
        this.whileExpression = exp;
        this.statement = stmt;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(this.whileExpression.deepCopy(), this.statement.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IStack<IStatement> exeStack = state.getExeStack();
        IHeap<IValue> heap = state.getHeap();
        IValue whileVal = this.whileExpression.eval(symTable, heap);
        if(!whileVal.getType().equals(new BoolType()))
            throw new MyException("While condition is not a boolean");
        if(((BoolValue)whileVal).getValue()) {
            exeStack.push(this);
            exeStack.push(this.statement);
        }
        return null;
    }
    @Override
    public String toString() {
        return "while(" + this.whileExpression.toString() + ") {\n" +
                this.statement.toString() + "}\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type = this.whileExpression.typecheck(typeEnv);
        if(type.equals(new BoolType())) {
            IDictionary<String, IType> typeEnvClone = new ADTDictionary<>();
            for(Map.Entry<String, IType> entry: typeEnv.getDictionary().entrySet()) {
                typeEnvClone.add(entry.getKey(), entry.getValue().deepCopy());
            }
            this.statement.typecheck(typeEnvClone);
            return typeEnv;
        }
        else
            throw new MyException("Type Checker Error: while expression is not of boolean type");
    }
}
