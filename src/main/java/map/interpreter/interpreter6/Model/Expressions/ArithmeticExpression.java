package map.interpreter.interpreter6.Model.Expressions;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.IntType;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

public class ArithmeticExpression implements IExpression {
    private final IExpression ex1;
    private final IExpression ex2;

    private final int operator; //1-plus, 2-minus, 3-star, 4-divide

    public ArithmeticExpression(IExpression e1, IExpression e2, int operator) {
        this.ex1 = e1;
        this.ex2 = e2;
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
        if(this.operator < 1 || this.operator > 4)
            throw new MyException("Invalid arithmetic operator");
        IntValue i1 = (IntValue) val1;
        IntValue i2 = (IntValue) val2;
        int n1, n2;
        n1 = i1.getValue();
        n2 = i2.getValue();
        return switch (this.operator) {
            case 1 -> new IntValue(n1 + n2);
            case 2 -> new IntValue(n1 - n2);
            case 3 -> new IntValue(n1 * n2);
            case 4 -> {
                if (n2 == 0)
                    throw new MyException("Division by zero attempted");
                yield new IntValue(n1 / n2);
            }
            default -> throw new MyException("Invalid arithmetic operator");
        };
    }

    @Override
    public String toString() {
        String result = "(" + this.ex1.toString()+ " ";
        result += switch(this.operator) {
            case 1 -> "+";
            case 2 -> "-";
            case 3 -> "*";
            case 4 -> "/";
            default -> " ";
        };
        result += " " + this.ex2.toString() + ")";
        return result;
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticExpression(this.ex1.deepCopy(), this.ex2.deepCopy(), this.operator);
    }

    @Override
    public IType typecheck(IDictionary<String,IType> typeEnv) throws MyException{
        IType type1, type2;
        type1 = ex1.typecheck(typeEnv);
        type2 = ex2.typecheck(typeEnv);
        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())){
                return new IntType();
            }
            else
                throw new MyException("Type Checker Error: second operand is not an integer");
        }
        else
            throw new MyException("Type Checker Error: first operand is not an integer");
    }
}
