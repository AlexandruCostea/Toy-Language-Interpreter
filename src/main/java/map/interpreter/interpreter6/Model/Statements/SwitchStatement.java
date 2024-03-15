package map.interpreter.interpreter6.Model.Statements;

import map.interpreter.interpreter6.Model.DataStructures.ADTs.ADTDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IStack;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Expressions.IExpression;
import map.interpreter.interpreter6.Model.Expressions.RelationalExpression;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Types.BoolType;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;

import java.util.Map;

public class SwitchStatement implements IStatement {

    private final IExpression expression;

    private final IExpression exp1;

    private final IStatement stmt1;

    private final IExpression exp2;

    private final IStatement stmt2;

    private final IStatement stmt3;

    public SwitchStatement(IExpression expression, IExpression exp1,
    IStatement stmt1, IExpression exp2, IStatement stmt2, IStatement stmt3) {
        this.expression = expression;
        this.exp1 = exp1;
        this.stmt1 = stmt1;
        this.exp2 = exp2;
        this.stmt2 = stmt2;
        this.stmt3 = stmt3;
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(this.expression.deepCopy(), this.exp1.deepCopy(),
                this.stmt1.deepCopy(), this.exp2.deepCopy(), this.stmt2.deepCopy(), this.stmt3.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStack<IStatement> stk = state.getExeStack();
        RelationalExpression exp1 = new RelationalExpression(this.expression,
                this.exp1, 3);
        RelationalExpression exp2 = new RelationalExpression(this.expression,
                this.exp2, 3);
        IfStatement stmt2 = new IfStatement(exp2, this.stmt2, this.stmt3);
        IfStatement stmt1 = new IfStatement(exp1, this.stmt1, stmt2);
        stk.push(stmt1);
        return null;
    }

    @Override
    public String toString() {
        return "switch(" + expression.toString() + ") {\n" +
                "(case " + exp1.toString() +"):\n" + stmt1.toString() +"\n" +
                "(case " + exp2.toString() + "):\n" + stmt2.toString() + "\n" +
                "(default):\n" + stmt3.toString() + "\n}";
    }

    @Override
    public IDictionary<String, IType> typecheck(IDictionary<String,IType> typeEnv) throws MyException {
        IType type = this.expression.typecheck(typeEnv);
        IType type2 = this.exp1.typecheck(typeEnv);
        IType type3 = this.exp2.typecheck(typeEnv);
        if(type.equals(type2) && type.equals(type3)) {
            IDictionary<String, IType> typeEnv1 = new ADTDictionary<>();
            IDictionary<String, IType> typeEnv2 = new ADTDictionary<>();
            IDictionary<String, IType> typeEnv3 = new ADTDictionary<>();
            for(Map.Entry<String, IType> entry: typeEnv.getDictionary().entrySet()) {
                typeEnv1.add(entry.getKey(), entry.getValue().deepCopy());
                typeEnv2.add(entry.getKey(), entry.getValue().deepCopy());
                typeEnv3.add(entry.getKey(), entry.getValue().deepCopy());
            }
            this.stmt1.typecheck(typeEnv1);
            this.stmt2.typecheck(typeEnv2);
            this.stmt3.typecheck(typeEnv3);
            return typeEnv;
        }
        else
            throw new MyException("Type Checker Error: switch expression parameters have different types");
    }
}
