package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.BoolType;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.IntType;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

public class RelationalExpression implements IExpression {
    private final IExpression ex1;
    private final IExpression ex2;

    private final int operator; // 1-> <; 2-> <=; 3-> ==; 4-> !=; 5-> >; 6-> >=

    public RelationalExpression(IExpression ex1, IExpression ex2, int operator) {
        this.ex1 = ex1;
        this.ex2 = ex2;
        this.operator = operator;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> table, IHeap<IValue> heap) throws MyException {
        IValue val1 = this.ex1.eval(table, heap);
        IValue val2 = this.ex2.eval(table, heap);
        if(!val1.getType().equals(new IntType()))
            throw new MyException("First operand is not an integer");
        if(!val2.getType().equals(new IntType()))
            throw new MyException("Second operand is not an integer");
        if(this.operator < 1 || this.operator > 6)
            throw new MyException("Invalid relational operator");
        IntValue i1 = (IntValue) val1;
        IntValue i2 = (IntValue) val2;
        return switch (this.operator) {
            case 1 -> new BoolValue(i1.getValue() < i2.getValue());
            case 2 -> new BoolValue(i1.getValue() <= i2.getValue());
            case 3 -> new BoolValue(i1.getValue() == i2.getValue());
            case 4 -> new BoolValue(i1.getValue() != i2.getValue());
            case 5 -> new BoolValue(i1.getValue() > i2.getValue());
            case 6 -> new BoolValue(i1.getValue() >= i2.getValue());
            default -> throw new MyException("Invalid relational operator");
        };
    }

    @Override
    public String toString() {
        String result = "(" + this.ex1.toString()+ " ";
        result += switch(this.operator) {
            case 1 -> "<";
            case 2 -> "<=";
            case 3 -> "==";
            case 4 -> "!=";
            case 5 -> ">";
            case 6 -> ">=";
            default -> " ";
        };
        result += " " + this.ex2.toString() + ")";
        return result;
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(this.ex1.deepCopy(), this.ex2.deepCopy(), this.operator);
    }

    @Override
    public IType typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type1, type2;
        type1 = ex1.typecheck(typeEnv);
        type2 = ex2.typecheck(typeEnv);
        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())){
                return new BoolType();
            }
            else
                throw new MyException("Type Checker Error: second operand is not an integer");
        }
        else
            throw new MyException("Type Checker Error: first operand is not an integer");
    }
}
