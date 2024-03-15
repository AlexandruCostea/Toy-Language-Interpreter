package map.interpreter.interpreter6.Model.Types;

import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;

public class BoolType implements IType{

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return "boolean";
    }

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public IType deepCopy() {
        return new BoolType();
    }
}
