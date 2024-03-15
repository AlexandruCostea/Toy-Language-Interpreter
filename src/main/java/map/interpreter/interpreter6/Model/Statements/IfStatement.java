package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IStack;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.BoolType;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;

import java.util.Map;

public class IfStatement implements IStatement {
    private final IExpression exp;
    private final IStatement ifStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression exp, IStatement ifSt, IStatement elSt) {
        this.exp = exp;
        this.ifStatement = ifSt;
        this.elseStatement = elSt;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(this.exp.deepCopy(), this.ifStatement.deepCopy(), this.elseStatement.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStack<IStatement> stk = state.getExeStack();
        IValue val = this.exp.eval(state.getSymTable(), state.getHeap());
        if(!val.getType().equals(new BoolType()))
            throw new MyException("Given condition is not a boolean");
        boolean boolVal = ((BoolValue) val).getValue();
        if(boolVal)
            stk.push(this.ifStatement);
        else
            stk.push(this.elseStatement);
        return null;
    }
    @Override
    public String toString() {
        return "if(" + exp.toString() + ") then {\n   " + ifStatement.toString() +
                "}\nelse{\n   " + elseStatement.toString()+"}\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type = this.exp.typecheck(typeEnv);
        if(type.equals(new BoolType())) {
            IDictionary<String, IType> typeEnv1 = new ADTDictionary<>();
            IDictionary<String, IType> typeEnv2 = new ADTDictionary<>();
            for(Map.Entry<String, IType> entry: typeEnv.getDictionary().entrySet()) {
                typeEnv1.add(entry.getKey(), entry.getValue().deepCopy());
                typeEnv2.add(entry.getKey(), entry.getValue().deepCopy());
            }
            this.ifStatement.typecheck(typeEnv1);
            this.elseStatement.typecheck(typeEnv2);
            return typeEnv;
        }
        else
            throw new MyException("Type Checker Error: if expression is not of boolean type");
    }
}
