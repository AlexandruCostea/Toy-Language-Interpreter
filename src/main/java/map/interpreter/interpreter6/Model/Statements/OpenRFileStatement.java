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
import java.io.FileReader;

public class OpenRFileStatement implements IStatement {
    private final IExpression fileName;

    public OpenRFileStatement(IExpression filename) {
        this.fileName = filename;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(fileName.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        IValue val = this.fileName.eval(symTable, heap);
        if(!val.getType().equals(new StringType()))
            throw new MyException("Given file name is not of type string");
        if(fileTable.containsKey((StringValue)val).getValue())
            throw new MyException("File is already open");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(((StringValue)val).getValue()));
            fileTable.add((StringValue)val, reader);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "OpenRFile(" + this.fileName.toString() + ");\n";
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
