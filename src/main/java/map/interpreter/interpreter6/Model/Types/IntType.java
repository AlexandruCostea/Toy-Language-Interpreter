package map.interpreter.interpreter6.Model.Types;

import map.interpreter.interpreter6.Model.Values.IntValue;
import map.interpreter.interpreter6.Model.Values.IValue;

public class IntType implements IType {

    @Override
    public boolean equals(Object another) {
        return another instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }

    @Override
    public IType deepCopy() {
        return new IntType();
    }
}
