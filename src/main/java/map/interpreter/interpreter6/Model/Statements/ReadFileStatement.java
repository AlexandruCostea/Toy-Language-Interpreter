package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.IntType;
import map.interpreter.interpreter6.Model.Types.StringType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;

public class ReadFileStatement implements IStatement {
    private final IExpression expression;
    private final StringValue variableName;

    public ReadFileStatement(IExpression exp, StringValue var) {
        this.expression = exp;
        this.variableName = var;
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(this.expression.deepCopy(), new StringValue(this.variableName.getValue()));
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap<IValue> heap = state.getHeap();
        if(!symTable.containsKey(this.variableName.getValue()).getValue())
            throw new MyException("Variable not defined");
        if(!symTable.getValue(this.variableName.getValue()).getType().equals(new IntType()))
            throw new MyException("Variable is not an integer");
        IValue val = this.expression.eval(symTable,heap);
        if(!val.getType().equals(new StringType()))
            throw new MyException("File name is not a string");
        StringValue file = (StringValue) val;
        if(!fileTable.containsKey(file).getValue())
            throw new MyException("File doesn't exist or hasn't been opened");
        BufferedReader reader = fileTable.getValue(file);
        try {
            String line = reader.readLine();
            IntValue intVal = (line == null) ? new IntValue(0) : new IntValue(Integer.parseInt(line));
            symTable.update(this.variableName.getValue(), intVal);
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "readFile(" + this.expression.toString() +"," + this.variableName.toString() + ");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type = this.expression.typecheck(typeEnv);
        if(type.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Type Checker Error: file name expression is not of type String");
    }
}
