package map.interpreter.interpreter6.Model.Types;

import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

public class StringType implements IType{
    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }
    @Override
    public IType deepCopy() {
        return new StringType();
    }
}
