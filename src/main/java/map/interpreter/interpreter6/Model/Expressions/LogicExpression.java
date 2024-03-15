package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.BoolType;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;

public class LogicExpression implements IExpression {
    private final IExpression ex1;
    private final IExpression ex2;
    private final int operator; // 1-> and; 2->or

    public LogicExpression(IExpression ex1, IExpression ex2, int operator) {
        this.ex1 = ex1;
        this.ex2 = ex2;
        this.operator = operator;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> table, IHeap<IValue> heap) throws MyException {
        IValue val1 = this.ex1.eval(table, heap);
        IValue val2 = this.ex2.eval(table, heap);
        if(!val1.getType().equals(new BoolType()))
            throw new MyException("First operand is not a boolean");
        if(!val2.getType().equals(new BoolType()))
            throw new MyException("Second operand is not a boolean");
        if(this.operator < 1 || this.operator > 2)
            throw new MyException("Invalid boolean operator");
        BoolValue i1 = (BoolValue) val1;
        BoolValue i2 = (BoolValue) val2;
        boolean n1 = i1.getValue();
        boolean n2 = i2.getValue();
        return switch (this.operator) {
            case 1 -> new BoolValue(n1 && n2);
            case 2 -> new BoolValue(n1 || n2);
            default -> throw new MyException("Invalid boolean operator");
        };
    }

    @Override
    public String toString() {
        String result = "(" + this.ex1.toString()+ " ";
        result += switch(this.operator) {
            case 1 -> "and";
            case 2 -> "or";
            default -> " ";
        };
        result += " " + this.ex2.toString() + ")";
        return result;
    }

    @Override
    public IExpression deepCopy() {
        return new LogicExpression(this.ex1.deepCopy(), this.ex2.deepCopy(), this.operator);
    }

    @Override
    public IType typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type1, type2;
        type1 = ex1.typecheck(typeEnv);
        type2 = ex2.typecheck(typeEnv);
        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())){
                return new BoolType();
            }
            else
                throw new MyException("Type Checker Error: second operand is not a boolean");
        }
        else
            throw new MyException("Type Checker Error: first operand is not a boolean");
    }
}
