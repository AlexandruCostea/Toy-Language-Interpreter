package map.interpreter.interpreter6.Model.Values;

import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.StringType;

public class StringValue implements IValue{
    private final String value;

    public StringValue() { value = ""; }

    public StringValue(String v) { value = v; }

    public String getValue() { return value; }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object another) {
        if (another instanceof StringValue)
            return value.equals(((StringValue) another).getValue());
        else
            return false;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    @Override
    public IType getType() { return new StringType(); }
    @Override
    public IValue deepCopy() { return new StringValue(value); }

}
