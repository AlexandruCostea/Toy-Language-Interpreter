package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IList;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

public class PrintStatement implements IStatement{

    private final IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(this.expression.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IList<IValue> out = state.getOut();
        IHeap<IValue> heap = state.getHeap();
        out.add(expression.eval(state.getSymTable(), heap));
        return null;
    }

    @Override
    public String toString() {
        return "Print(" +expression.toString()+");\n";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        this.expression.typecheck(typeEnv);
        return typeEnv;
    }
}