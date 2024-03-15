package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.StringType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStatement implements IStatement {
    private final IExpression fileName;

    public CloseRFileStatement(IExpression fileName) {
        this.fileName = fileName;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(this.fileName.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap<IValue> heap = state.getHeap();
        IValue val = this.fileName.eval(state.getSymTable(),heap);
        if(!val.getType().equals(new StringType()))
            throw new MyException("Invalid file name");
        StringValue file = (StringValue) val;
        if(!fileTable.containsKey(file).getValue())
            throw new MyException("File doesn't exist or is not opened");
        BufferedReader reader = fileTable.getValue(file);
        try {
            reader.close();
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }
        fileTable.remove(file);
        return null;
    }

    @Override
    public String toString() {
        return "closeRFile(" + this.fileName.toString() + ");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type = this.fileName.typecheck(typeEnv);
        if(type.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Type Checker Error: file name expression is not of type String");
    }
}
