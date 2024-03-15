package map.interpreter.interpreter6.Model.Values;


import map.interpreter.interpreter6.Model.Types.BoolType;
import map.interpreter.interpreter6.Model.Types.IType;

public class BoolValue implements IValue{
    private final boolean value;

    public BoolValue() {
        this.value = false;
    }

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Boolean.toString(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BoolValue)
            return this.value == ((BoolValue)obj).value;
        return false;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(this.value);
    }
}
